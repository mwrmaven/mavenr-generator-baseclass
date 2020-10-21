package com.mavenr.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mavenr
 * @Classname ColumnEnum
 * @Description 字段类型枚举类
 * @Date 2020/10/16 11:32 下午
 */
@AllArgsConstructor
@Getter
public enum ColumnEnum{
    VARCHAR("VARCHAR", "String"),
    INTEGER("INTEGER", "Integer"),
    INT("INT", "Integer"),
    DATE("DATE", "Date"),
    TIMESTAMP("TIMESTAMP", "Date"),
    VARCHAR2("VARCHAR2", "String"),
    NUMBER("NUMBER", "Integer");

    private String columnType;
    private String propertyType;

    public static ColumnEnum getColumnType(String columnType) {
        ColumnEnum[] columnEnums = ColumnEnum.values();
        for (ColumnEnum columnEnum : columnEnums) {
            if (columnEnum.getColumnType().equalsIgnoreCase(columnType)) {
                return columnEnum;
            }
        }
        return null;
    }
}
