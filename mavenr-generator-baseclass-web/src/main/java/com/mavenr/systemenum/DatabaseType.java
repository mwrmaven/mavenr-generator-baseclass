package com.mavenr.systemenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mavenr
 * @Classname DatabaseType
 * @Description 数据库类型
 * @Date 2022/8/17 14:15
 */
@AllArgsConstructor
@Getter
public enum DatabaseType {

    ORACLE(1, "ORACLE"),
    MYSQL(2, "MYSQL"),
    DMDB(3, "DMDB");

    private int id;

    private String type;
}
