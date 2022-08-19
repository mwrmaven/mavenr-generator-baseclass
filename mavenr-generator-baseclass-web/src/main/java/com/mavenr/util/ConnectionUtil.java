package com.mavenr.util;


import com.mavenr.entity.BaseConfig;
import com.mavenr.enums.DatabaseTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author mavenr
 * @Classname ConnectionUtil
 * @Description 数据库连接工具类，生成和关闭Connection连接
 * @Date 2020/10/16 11:30 下午
 */
public class ConnectionUtil {
    /**
     * 获取数据库连接
     * @param baseConfig
     * @return
     */
    public static Connection getConnectionNew(BaseConfig baseConfig) {
        // 数据库连接url
        String url = "";
        String driverClassName = "";
        String address = baseConfig.getPath();
        String port = baseConfig.getPort();
        String dbName = baseConfig.getName();
        String type = baseConfig.getType();
        // 判断数据库类型
        if (DatabaseTypeEnum.ORACLE.getType().equals(type)) {
            // Oracle
            url = "jdbc:oracle:thin:@" + address + ":" + port + ":" + dbName;
            driverClassName = "oracle.jdbc.OracleDriver";
        } else if (DatabaseTypeEnum.MYSQL.getType().equals(type)) {
            // Mysql
            url = "jdbc:mysql://" + address + ":" + port + "/" + dbName + "?serverTimezone=UTC&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&useSSL=FALSE";
            driverClassName = "com.mysql.cj.jdbc.Driver";
        }

        // 判断url地址
        if ("".equals(url) || "".equals(url.trim())) {
            return null;
        }

        Connection connection = null;
        String username = baseConfig.getUser();
        String password = baseConfig.getPwd();
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            if (DatabaseTypeEnum.ORACLE.getType().equals(type)) {
                url = "jdbc:oracle:thin:@" + address + ":" + port + "/" + dbName;
                try {
                    connection = DriverManager.getConnection(url, username, password);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     * @param connection
     */
    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
