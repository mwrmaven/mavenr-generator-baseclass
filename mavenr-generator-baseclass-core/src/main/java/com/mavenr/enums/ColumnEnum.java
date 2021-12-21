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
    // 数据库中的VARCHAR类型
    VARCHAR("VARCHAR", "String", "java.lang.String"),
    // 数据库中的INTEGER类型
    INTEGER("INTEGER", "Integer", "java.lang.Integer"),
    // 数据库中的INT类型
    INT("INT", "Integer", "java.lang.Integer"),
    // 数据库中的DATE类型
    DATE("DATE", "Date", "java.util.Date"),
    // 数据库中的TIMESTAMP类型
    TIMESTAMP("TIMESTAMP", "Date", "java.util.Date"),
    // 数据库中的VARCHAR2类型
    VARCHAR2("VARCHAR2", "String", "java.lang.String"),
    // 数据库中的NUMBER类型
    NUMBER("NUMBER", "Integer", "java.lang.Integer");

    /**
     * 数据库字段类型
     */
    private String columnType;
    /**
     * 类属性类型
     */
    private String propertyType;
    /**
     * 类型路径
     */
    private String classPath;

    /**
     * 根据数据库字段类型查询枚举类
     *
     * @param columnType
     * @return
     */
    public static ColumnEnum getColumnType(String columnType) {
        ColumnEnum[] columnEnums = ColumnEnum.values();
        for (ColumnEnum columnEnum : columnEnums) {
            if (columnEnum.getColumnType().equalsIgnoreCase(columnType)) {
                return columnEnum;
            }
        }
        return null;
    }

    /**
     * 根据类属性类型查询枚举类
     *
     * @param propertyType
     * @return
     */
    public static ColumnEnum getPropertyType(String propertyType) {
        ColumnEnum[] columnEnums = ColumnEnum.values();
        for (ColumnEnum columnEnum : columnEnums) {
            if (columnEnum.getPropertyType().equalsIgnoreCase(propertyType)) {
                return columnEnum;
            }
        }
        return null;
    }
}
