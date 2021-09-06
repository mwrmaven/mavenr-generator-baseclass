package com.mavenr.util;


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
@Slf4j
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
    public static Connection getConnection(String type, String address, String port, String dbName, String username, String password, String version) {
        String driver = "";
        String url = "";
        if (DatabaseTypeEnum.ORACLE.getType().equalsIgnoreCase(type)) {
            driver = "oracle.jdbc.OracleDriver";
            url = "jdbc:oracle:thin:@" + address + ":" + port + ":" + dbName;
            int versionFlag = 11;
            String versionStr = version;
            if (versionStr.trim().length() >= 2) {
                versionStr = versionStr.trim().substring(0, 2);
            }
            try {
                if (Integer.valueOf(versionStr) > versionFlag) {
                    url = "jdbc:oracle:thin:@" + address + ":" + port + "/" + dbName;
                }
            } catch (Exception e) {
                log.error("Oracle数据库版本输入错误！");
            }

        } else if (DatabaseTypeEnum.MYSQL.getType().equalsIgnoreCase(type)) {
            driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://" + address + ":" + port + "/" + dbName + "?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&useSSL=FALSE";
            int versionFlag = 5;
            String[] versions = version.trim().split("\\.");
            try {
                if (Integer.valueOf(versions[0]) > versionFlag) {
                    driver = "com.mysql.cj.jdbc.Driver";
                }
            } catch (Exception e) {
                log.error("Mysql数据库版本输入错误！");
            }
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
