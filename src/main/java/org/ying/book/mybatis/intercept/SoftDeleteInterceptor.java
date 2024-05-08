package org.ying.book.mybatis.intercept;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, org.apache.ibatis.session.RowBounds.class, org.apache.ibatis.session.ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SoftDeleteInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取执行的方法名
        String methodName = invocation.getMethod().getName();

        // 如果是查询方法，修改 SQL 语句以过滤软删除记录
        if ("query".equals(methodName)) {
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];
            // 这里你可以根据需要检查参数，判断是否需要过滤软删除记录
            // 假设软删除标志字段为 deleted
            String originalSql = ms.getBoundSql(parameter).getSql();
            String newSql = originalSql + " WHERE deleted = 0"; // 添加软删除记录过滤条件
            BoundSql boundSql = ms.getBoundSql(newSql);
            MappedStatement newMs = MappedStatementUtils.copyFromMappedStatement(ms, new CustomSqlSource(ms.getConfiguration(), boundSql));
            invocation.getArgs()[0] = newMs;
        }

        // 执行原始方法
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在此方法中设置拦截器的属性
    }
}
