package com.mavenr.util;


import com.mavenr.enums.DatabaseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author mavenr
 * @Classname ConnectionUtil
 * @Description 数据库连接工具类，生成和关闭Connection连接
 * @Date 2020/10/16 11:30 下午
 */
@Slf4j
public class ConnectionUtil {
    /**
     * 获取数据库连接
     * @param driverClassName
     * @param address
     * @param port
     * @param dbName
     * @param username
     * @param password
     * @return
     */
    public static Connection getConnectionNew(String driverClassName, String address, String port, String dbName,
                                              String username, String password) {
        // 数据库连接url
        String url = "";
        String type = "";
        // 判断数据库类型
        if (driverClassName.toUpperCase().contains(DatabaseTypeEnum.ORACLE.getType())) {
            type = DatabaseTypeEnum.ORACLE.getType();
            // Oracle
            url = "jdbc:oracle:thin:@" + address + ":" + port + ":" + dbName;
        } else if (driverClassName.toUpperCase().contains(DatabaseTypeEnum.MYSQL.getType())) {
            type = DatabaseTypeEnum.MYSQL.getType();
            // Mysql
            url = "jdbc:mysql://" + address + ":" + port + "/" + dbName + "?serverTimezone=UTC&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&useSSL=FALSE";
        }

        // 判断url地址
        if (StringUtils.isBlank(url)) {
            return null;
        }

        Connection connection = null;
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
