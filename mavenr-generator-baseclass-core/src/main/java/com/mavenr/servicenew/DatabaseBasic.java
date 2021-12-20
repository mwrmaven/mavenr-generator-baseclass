package com.mavenr.servicenew;

import com.mavenr.entity.Table;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author mavenr
 * @Classname DatabaseBasic
 * @Description 数据库基础类，实现close方法
 * @Date 2021/12/20 17:14
 */
public abstract class DatabaseBasic implements DatabaseInterface {

    /**
     * 数据库连接
     */
    public Connection connection;

    @Override
    public void init(String configFilePath) {
        // 加载properties配置文件
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(new File(configFilePath)), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        // 数据库表名（若database.scanAllTables=true，则该条不生效）
        String tableName = properties.getProperty("database.tableName");
        // 数据库表名注释
        String tableNameCn = properties.getProperty("database.tableNameCn");
        // 是否扫描数据库中所有的表
        String scanAllTables = properties.getProperty("database.scanAllTables");
        // 输出路径
        String outPath = properties.getProperty("database.outPath");
        // 类所在的包基础路径（即entity、vo、server之前的包路径）
        String packagePath = properties.getProperty("database.packagePath");
        // 数据库版本
        String version = properties.getProperty("database.version");






    }

    /**
     * 实现数据库连接的关闭
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
