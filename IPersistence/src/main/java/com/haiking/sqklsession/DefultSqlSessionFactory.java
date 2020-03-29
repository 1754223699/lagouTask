package com.haiking.sqklsession;

import com.haiking.vo.Configuration;

public class DefultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSqlSession() {

        return new DefaultSqlSession(configuration);
    }

}
