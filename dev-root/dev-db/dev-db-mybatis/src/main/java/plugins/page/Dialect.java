package plugins.page;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.Properties;

public interface Dialect {

    /**
     * 生成查询总记录数sql
     *
     * @return
     */
    String getTotalCountSql(BoundSql boundSql);

    /**
     * 获得总记录数
     *
     * @return
     * @throws SQLException
     */
    Long getTotalCount(Executor executor, MappedStatement ms, Object parameterObject, BoundSql boundSql, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;

    /**
     * 生成查询分页sql
     *
     * @return
     */
    String getPageSql(BoundSql boundSql, Page<?> page);

    /**
     * 获得分页结果数据
     *
     * @return
     */
    Object getPageResult(Executor executor, MappedStatement ms, Page<?> page, Object parameterObject, BoundSql boundSql, CacheKey cacheKey, ResultHandler resultHandler) throws SQLException;

    /**
     * 配置信息
     */
    void setProperties(Properties variables);
}
