package com.mavenr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mavenr
 * @Classname DatabaseTypeEnum
 * @Description 数据库类型枚举类
 * @Date 2020/10/16 11:32 下午
 */
@AllArgsConstructor
@Getter
public enum DatabaseTypeEnum{
    // oracle数据库
    ORACLE("ORACLE", 1),
    // mysql数据库
    MYSQL("MYSQL", 2);

    /**
     * 数据库的类型
     */
    private String type;
    /**
     * 类型对应的值
     */
    private Integer code;

    public static DatabaseTypeEnum getType(String type) {
        DatabaseTypeEnum[] databaseTypeEnums = DatabaseTypeEnum.values();
        for (DatabaseTypeEnum databaseTypeEnum : databaseTypeEnums) {
            if (databaseTypeEnum.getType().equalsIgnoreCase(type)) {
                return databaseTypeEnum;
            }
        }
        return null;
    }
}
