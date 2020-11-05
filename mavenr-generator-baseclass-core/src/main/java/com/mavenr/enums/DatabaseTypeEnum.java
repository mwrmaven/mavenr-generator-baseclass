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
    ORACLE("ORACLE", 1),
    MYSQL("MYSQL", 2);
    private String type;
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
