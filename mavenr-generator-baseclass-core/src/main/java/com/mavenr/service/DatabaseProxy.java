package com.mavenr.service;

import com.mavenr.enums.DatabaseTypeEnum;

/**
 * @author mavenr
 * @Classname DatabaseProxy
 * @Description 数据库解析代理类
 * @Date 2020/10/17 10:47 下午
 */
public class DatabaseProxy {

    public static DatabaseAbstract getDatabaseService (String type) {
        if (DatabaseTypeEnum.ORACLE.getType().equalsIgnoreCase(type)) {
            return new OracleDatabase();
        } else if (DatabaseTypeEnum.MYSQL.getType().equalsIgnoreCase(type)) {
            return new MysqlDatabase();
        } else {
            return null;
        }
    }
}
