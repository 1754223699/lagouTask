package com.haiking.sqklsession;

import java.io.InputStream;

import com.haiking.conifg.XMLConfigBuilder;
import com.haiking.vo.Configuration;

public class SqlSessionFactoryBuiler {
    public SqlSessionFactory build(InputStream inputStream) throws Exception {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        //解析好的configuration：包含数据源和sql的mapper解析
        Configuration parseConfiguration = xmlConfigBuilder.parseConfiguration(inputStream);

        //sqlSessionFactory 工厂类，生产SQLSession这个会话对象
        DefultSqlSessionFactory defultSqlSessionFactory = new DefultSqlSessionFactory(parseConfiguration);

        return defultSqlSessionFactory;
    }
}
