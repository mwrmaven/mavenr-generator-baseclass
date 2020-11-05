package com.mavenr.util;


import com.mavenr.enums.DatabaseTypeEnum;

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
     * 根据数据库信息创建连接
     *
     * @param type
     * @param address
     * @param port
     * @param dbName
     * @param username
     * @param password
     * @return
     */
    public static Connection getConnection(String type, String address, String port, String dbName, String username, String password) {
        String driver = "";
        String url = "";
        if (DatabaseTypeEnum.ORACLE.getType().equalsIgnoreCase(type)) {
            driver = "oracle.jdbc.OracleDriver";
            url = "jdbc:oracle:thin:@" + address + ":" + port + "/" + dbName;
        } else if (DatabaseTypeEnum.MYSQL.getType().equalsIgnoreCase(type)) {
            driver = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://" + address + ":" + port + "/" + dbName + "?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&useSSL=FALSE";
        }

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接
     *
     * @param connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
