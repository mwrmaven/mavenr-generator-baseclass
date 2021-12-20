package com.mavenr.servicenew;

import com.mavenr.entity.Table;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * @author mavenr
 * @Classname MySqlDatabase
 * @Description Mysql数据库的数据表信息获取
 * @Date 2021/12/20 17:19
 */
public class MySqlDatabase extends DatabaseBasic{
    @Override
    public Connection getConnection(Properties properties) {
        connection =
        return null;
    }

    @Override
    public List<Table> columns(List<String> tableNames) {



        close();

        return null;
    }
}
