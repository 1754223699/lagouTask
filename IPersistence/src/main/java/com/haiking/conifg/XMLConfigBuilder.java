package com.haiking.conifg;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.haiking.io.Resources;
import com.haiking.vo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class XMLConfigBuilder {
    private Configuration configuration;

    public XMLConfigBuilder() {
        configuration = new Configuration();
    }

    @SuppressWarnings("unchecked")
    public Configuration parseConfiguration(InputStream inputStream) throws Exception {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> selectNodes = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element selectElement : selectNodes) {
            String name = selectElement.attributeValue("name");
            String value = selectElement.attributeValue("value");
            properties.setProperty(name, value);
        }
        //数据库
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);


        //mapper.xml解析：拿到路径--》字节输入流-》解析
        List<Element> mapperList = rootElement.selectNodes("mappers");
        for (Element mapperElement : mapperList) {
            String mapperPath = mapperElement.attributeValue("resources");
            InputStream mapperInputStream = Resources.getResourcesAsStream(mapperPath);
            XMLMapperBuilder xlmlMapperBuilder = new XMLMapperBuilder(configuration);
            xlmlMapperBuilder.parse(mapperInputStream);
        }
        return configuration;
    }
}
