package plugins.page;


import java.util.Map;

public class Page<T> {

    private int pageNo;
    private int pageSize;
    private long totalCount;
    private int totalPage;
    private boolean isTotal;
    private DialectEnum dialectEnum;
    private Map<String, Object> params;
    private T results;

    // Mysql及Mariadb
    private int offset;
    private int rows;

    private Page(){}

    public static class PageBuilder<T> {
        private Page<T> page = new Page();

        public PageBuilder pageNo(int pageNo) {
            if (pageNo <= 0) {
                throw new MybatisPluginRuntimeException(MybatisPluginRuntimeException.ex_01, "Exception Message is [pageNo=" + pageNo + "]");
            }
            page.pageNo = pageNo;
            return this;
        }

        public PageBuilder pageSize(int pageSize) {
            if (pageSize <= 0) {
                throw new MybatisPluginRuntimeException(MybatisPluginRuntimeException.ex_02, "Exception Message is [pageSize=" + pageSize + "]");
            }
            page.pageSize = pageSize;
            return this;
        }

        public PageBuilder isTotal(boolean isTotal) {
            page.isTotal = isTotal;
            return this;
        }

        public PageBuilder dialectEnum(DialectEnum dialectEnum) {
            page.dialectEnum = dialectEnum;
            return this;
        }

        public PageBuilder params(Map<String, Object> params) {
            page.params = params;
            return this;
        }

        public Page<T> build() {
            validate();
            return page;
        }

        private void validate(){
            if(page.pageNo <= 0){
                throw new MybatisPluginRuntimeException(MybatisPluginRuntimeException.ex_03, "Exception Message is [pageNo is required]");
            }

            if(page.pageSize <= 0){
                page.pageSize = 20;
            }

            if(page.dialectEnum == null){
                page.dialectEnum = DialectEnum.MYSQL;
            }
        }
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalCount() {
        if(!isTotal()){
            totalCount = -1;// 未查询总记录数
        }
        return totalCount;
    }

    public int getTotalPage() {
        if(isTotal()){
            totalPage =  (int) ((totalCount / pageSize) + ((totalCount % pageSize == 0) ? 0 : 1));
        }else{
            totalPage = -1;// 未查询总记录数
        }

        return totalPage;
    }

    public boolean isTotal() {
        return isTotal;
    }

    public DialectEnum getDialectEnum() {
        return dialectEnum;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public T getResults() {
        return results;
    }

    public int getOffset() {
        switch (getDialectEnum()){
            case MYSQL:
            case MARIADB:
                offset = ((getPageNo() - 1) * getPageSize());
                break;
        }
        return offset;
    }

    public int getRows() {
        switch (getDialectEnum()){
            case MYSQL:
            case MARIADB:
                rows = getPageSize();
                break;
        }
        return rows;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
