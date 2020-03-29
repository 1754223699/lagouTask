package com.haiking.sqklsession;

import java.util.List;
import java.util.Map;

import com.haiking.vo.Configuration;
import com.haiking.vo.MapperStatement;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> List<E> selectAll(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MapperStatement> mapperStatementMap = configuration.getMapperStatementMap();
        MapperStatement mapperStatement = mapperStatementMap.get(statementId);
        List<Object> list = simpleExecutor.query(configuration, mapperStatement, params);

        return (List<E>) list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> all = selectAll(statementId, params);
        if (null == all || all.size() != 1) {
            throw new RuntimeException("查询结果异常");
        }
        return (T) all.get(0);
    }

    @Override
    public Integer deleteOne(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MapperStatement> mapperStatementMap = configuration.getMapperStatementMap();
        MapperStatement mapperStatement = mapperStatementMap.get(statementId);
        Integer deleteRows = simpleExecutor.delete(configuration, mapperStatement, params);
        return deleteRows;

    }

    @Override
    public Integer insertOne(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MapperStatement> mapperStatementMap = configuration.getMapperStatementMap();
        MapperStatement mapperStatement = mapperStatementMap.get(statementId);
        Integer insertRow = simpleExecutor.insert(configuration, mapperStatement, params);
        return insertRow;
    }

    @Override
    public Integer updateOne(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        Map<String, MapperStatement> mapperStatementMap = configuration.getMapperStatementMap();
        MapperStatement mapperStatement = mapperStatementMap.get(statementId);
        Integer insertRow = simpleExecutor.update(configuration, mapperStatement, params);
        return insertRow;
    }
}
