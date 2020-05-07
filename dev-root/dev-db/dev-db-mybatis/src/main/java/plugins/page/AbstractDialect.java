package plugins.page;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractDialect implements Dialect {

    protected Properties properties;
    protected Field additionalParameters;
    {
        try {
            additionalParameters = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParameters.setAccessible(true);
        } catch (NoSuchFieldException e) {

            throw new MybatisPluginRuntimeException(MybatisPluginRuntimeException.ex_04, "获取属性[BoundSql.additionalParameters]异常", e);
        }
    }

    protected Map<String, Object> getAdditianalParameters(BoundSql boundSql){
        try {
            return (Map<String, Object>) additionalParameters.get(boundSql);
        } catch (IllegalAccessException e) {

            throw new MybatisPluginRuntimeException(MybatisPluginRuntimeException.ex_05, "获取属性值[BoundSql.additionalParameters]异常", e);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }


    public Long getTotalCount(Executor executor, MappedStatement ms, Object parameterObject, BoundSql boundSql, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        // 创建一个返回Long类型的MS
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, Collections.EMPTY_LIST).build();
        resultMaps.add(resultMap);
        String msId = ms.getId() + "-TotalCount";
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), msId, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(resultMaps);
        builder.fetchSize(ms.getFetchSize());
        builder.timeout(ms.getTimeout());
        builder.statementType(ms.getStatementType());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        if (ms.getKeyColumns() != null && ms.getKeyColumns().length != 0) {
            StringBuilder keyColumns = new StringBuilder();
            for (String keyColumn : ms.getKeyColumns()) {
                keyColumns.append(keyColumn).append(",");
            }
            keyColumns.delete(keyColumns.length() - 1, keyColumns.length());
            builder.keyColumn(keyColumns.toString());
        }
        MappedStatement totalCountMs = builder.build();

        // 创建总记录数查询的缓存key
        CacheKey totalCountCacheKey = executor.createCacheKey(totalCountMs, parameterObject, RowBounds.DEFAULT, boundSql);

        String totalCountSql = getTotalCountSql(boundSql);

        BoundSql totalCountBoundSql = new BoundSql(totalCountMs.getConfiguration(), totalCountSql, boundSql.getParameterMappings(), parameterObject);

        Map<String, Object> additianalParameters = getAdditianalParameters(boundSql);
        for (String key : additianalParameters.keySet()) {
            totalCountBoundSql.setAdditionalParameter(key, additianalParameters.get(key));
        }

        // 执行总记录数查询
        List<Object> query = executor.query(totalCountMs, parameterObject, RowBounds.DEFAULT, resultHandler, totalCountCacheKey, totalCountBoundSql);
        Long totalCount = (Long) (query.get(0));
        return totalCount;
    }

    @Override
    public Object getPageResult(Executor executor, MappedStatement ms, Page<?> page, Object parameterObject, BoundSql boundSql, CacheKey cacheKey, ResultHandler resultHandler) throws SQLException {
        cacheKey.update(page.getOffset());
        cacheKey.update(page.getPageSize());

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if(parameterMappings != null){
            List<ParameterMapping> newParameterMappings = new ArrayList<>(parameterMappings);

            Configuration configuration = ms.getConfiguration();
            newParameterMappings.add(new ParameterMapping.Builder(configuration, "offset", Integer.class).build());
            newParameterMappings.add(new ParameterMapping.Builder(configuration, "rows", Integer.class).build());

            MetaObject metaObject = MetaObject.forObject(boundSql, configuration.getObjectFactory(), configuration.getObjectWrapperFactory(), configuration.getReflectorFactory());
            metaObject.setValue("parameterMappings", newParameterMappings);

            // todo modify
            BoundSql pageBoundSql = new BoundSql(configuration, getPageSql(boundSql, page), boundSql.getParameterMappings(), parameterObject);
            Map<String, Object> additianalParameters = getAdditianalParameters(boundSql);
            for (String key : additianalParameters.keySet()) {
                pageBoundSql.setAdditionalParameter(key, additianalParameters.get(key));
            }

            // 执行总记录数查询
            List<Object> result = executor.query(ms, parameterObject, RowBounds.DEFAULT, resultHandler, cacheKey, pageBoundSql);
            return result;
        }
        return null;
    }
}
