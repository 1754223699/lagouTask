package com.haiking.spring.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.haiking.spring.annotation.MyAutowired;
import com.haiking.spring.annotation.MyService;

@MyService(name="connectionUtils")
public class ConnectionUtils {
	
	@MyAutowired
    private DataSource dataSource;
	
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>(); // 存储当前线程的连接

    /**
     * 从当前线程获取连接
     */
    public Connection getCurrentThreadConn() throws SQLException {
        /**
         * 判断当前线程中是否已经绑定连接，如果没有绑定，需要从连接池获取一个连接绑定到当前线程
          */
        Connection connection = threadLocal.get();
        if(connection == null) {
            // 从连接池拿连接并绑定到线程
            connection = dataSource.getConnection();
            // 绑定到当前线程
            threadLocal.set(connection);
        }
        return connection;
    }
}
