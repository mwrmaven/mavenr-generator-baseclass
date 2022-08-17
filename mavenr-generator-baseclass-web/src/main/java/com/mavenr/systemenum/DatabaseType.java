package com.mavenr.systemenum;

import com.mavenr.window.DatabaseConfig;

/**
 * @author mavenr
 * @Classname DatabaseType
 * @Description 数据库类型
 * @Date 2022/8/17 14:15
 */
public enum DatabaseType {

    ORACLE(1, "ORACLE"), MYSQL(2, "MYSQL");

    private int id;

    private String type;

    DatabaseType() {

    }
    DatabaseType(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
