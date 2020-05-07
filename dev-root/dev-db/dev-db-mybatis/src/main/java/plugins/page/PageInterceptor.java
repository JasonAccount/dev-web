package plugins.page;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class PageInterceptor implements Interceptor {
    private Properties variables;
    private Dialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        BoundSql boundSql = null;
        CacheKey cacheKey = null;
        Executor executor = (Executor)invocation.getTarget();
        if(args.length == 4){
            boundSql = ms.getBoundSql(parameterObject);
            cacheKey = executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);
        }else{
            boundSql = (BoundSql) args[4];
            cacheKey = (CacheKey) args[5];
        }

        // 是否符合分页拦截规则(匹配方法后缀且参数为Page<?>对象)
        if(ms.getId().endsWith(variables.getProperty("methodSuffix"))){
            Page<?> page = null;
            if(parameterObject instanceof Map){
                Map<String, Object> paramMap = (Map<String, Object>) parameterObject;
                if(paramMap.containsKey("page")){
                    page = (Page<?>) paramMap.get("page");
                    paramMap.put("offset", page.getOffset());
                    paramMap.put("rows", page.getRows());
                }
            }else if(parameterObject instanceof Page<?>){
                page = (Page<?>) parameterObject;
            }

            if(page != null){
                if(page.isTotal()){// 查询总记录数
                    Long totalCount = dialect.getTotalCount(executor, ms, parameterObject, boundSql, rowBounds, resultHandler);
                    page.setTotalCount(totalCount.longValue());
                }
                // 查询分页结果


            }
        }


        return executor.query(ms, parameterObject, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public Object plugin(Object o) {

        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.variables = properties;
        String methodSuffix = variables.getProperty("methodSuffix");
        if(StringUtils.isBlank(methodSuffix)){
            methodSuffix = "ByPage";
        }
        variables.setProperty("methodSuffix", methodSuffix);

        String dia = properties.getProperty("dialect");
        if(StringUtils.isBlank(dia)){
            dia = "MYSQL";
        }
        DialectEnum dialectEnum = DialectEnum.valueOf(dia);
        switch (dialectEnum){
            case ORACLE:
                break;
            default:
                dialect = new MysqlDialect();
        }
        dialect.setProperties(this.variables);
    }
}
