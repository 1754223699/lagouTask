package com.haiking.conifg;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.haiking.vo.Configuration;
import com.haiking.vo.MapperStatement;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    public void parse(InputStream inputStream) throws Exception {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        //获取查询的sql
        List<Element> selectNodes = rootElement.selectNodes("//select");
        for (Element selectNode : selectNodes) {
            fillMapperStatement(namespace, selectNode);
        }

        //获取删除的sql
        List<Element> deleteElements = rootElement.selectNodes("//delete");
        for (Element deleteElement : deleteElements) {
            fillMapperStatement(namespace, deleteElement);
        }

        //获取插入的sql
        List<Element> insertElemets = rootElement.selectNodes("//insert");
        for (Element insertElement : insertElemets) {
            fillMapperStatement(namespace, insertElement);
        }
        //获取更新的sql
        List<Element> updateElements = rootElement.selectNodes("//update");
        for (Element updateElement : updateElements) {
            fillMapperStatement(namespace, updateElement);
        }
    }

    private void fillMapperStatement(String namespace, Element element) {
        String id = element.attributeValue("id");
        String parameterType = element.attributeValue("parameterType");
        String resultType = element.attributeValue("resultType");
        String sqlText = element.getTextTrim();
        MapperStatement mapperStatement = new MapperStatement();
        mapperStatement.setId(id);
        mapperStatement.setSql(sqlText);
        mapperStatement.setResultType(resultType);
        mapperStatement.setParameterType(parameterType);
        String mapKey = namespace + "." + id;
        configuration.getMapperStatementMap().put(mapKey, mapperStatement);
    }

}
