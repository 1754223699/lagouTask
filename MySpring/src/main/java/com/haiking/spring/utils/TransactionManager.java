package com.haiking.spring.utils;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.haiking.spring.annotation.MyAutowired;

@Component("transactionManager")
public class TransactionManager {
	
	@MyAutowired
    private ConnectionUtils connectionUtils;

    // 开启手动事务控制
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }

    // 提交事务
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }

    // 回滚事务
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }
}
