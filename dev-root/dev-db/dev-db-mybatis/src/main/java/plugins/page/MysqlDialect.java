package plugins.page;

import org.apache.ibatis.mapping.BoundSql;

public class MysqlDialect extends AbstractDialect {

    @Override
    public String getTotalCountSql(BoundSql boundSql) {
        // 查询总记录SQL
        String sql = boundSql.getSql();
        StringBuilder sb = new StringBuilder(sql.length() + 50);
        return sb.append("select count(")
                .append("1")
                .append(") from (")
                .append(sql)
                .append(") tmp_total_count")
                .toString();
    }

    @Override
    public String getPageSql(BoundSql boundSql, Page<?> page) {
        // 查询分页SQL
        String sql = boundSql.getSql();
        StringBuilder sb = new StringBuilder(sql.length() + 50);
        return sb.append(sql)
                .append("1")
                .append(") from (")
                .append(sql)
                .append(") tmp_total_count")
                .toString();
    }
}
