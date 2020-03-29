package com.haiking.sqklsession;

import java.util.List;

public interface SqlSession {
    //查询所有
    public <E> List<E> selectAll(String statementId, Object... params) throws Exception;

    //根据条件查询单个
    public <T> T selectOne(String statementId, Object... params) throws Exception;

    //删除单个
    public Integer deleteOne(String statementId, Object... params) throws Exception;

    //添加单个
    public Integer insertOne(String statementId, Object... params) throws Exception;

    //更新单个
    public Integer updateOne(String statementId, Object... params) throws Exception;
}
