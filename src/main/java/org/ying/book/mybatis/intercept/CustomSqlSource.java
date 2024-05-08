package org.ying.book.mybatis.intercept;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

public class CustomSqlSource implements SqlSource {

    private final Configuration configuration;
    private final BoundSql boundSql;

    public CustomSqlSource(Configuration configuration, BoundSql boundSql) {
        this.configuration = configuration;
        this.boundSql = boundSql;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return boundSql;
    }
}
