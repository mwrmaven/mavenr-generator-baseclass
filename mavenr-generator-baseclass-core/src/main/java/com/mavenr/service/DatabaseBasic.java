package com.mavenr.service;

import com.mavenr.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author mavenr
 * @Classname DatabaseBasic
 * @Description 数据库基础类，实现close方法
 * @Date 2021/12/20 17:14
 */
public abstract class DatabaseBasic implements DataInfoInterface {

    /**
     * 数据库连接
     */
    public Connection connection;

    @Override
    public void init(Properties properties) {
        // 数据库地址
        String address = properties.getProperty("database.address");
        // 数据库端口号
        String port = properties.getProperty("database.port");
        // 数据库名称
        String dbName = properties.getProperty("database.dbName");
        // 数据库用户名
        String username = properties.getProperty("database.username");
        // 数据库密码
        String password = properties.getProperty("database.password");
        // 数据库驱动
        String driverClassName = properties.getProperty("database.driverClassName");
        System.out.println(driverClassName);
        connection = ConnectionUtil.getConnectionNew(driverClassName, address, port, dbName, username, password);
    }

    /**
     * 实现数据库连接的关闭
     */
    public void close() {
        ConnectionUtil.close(connection);
    }
}
