package com.mavenr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mavenr
 * @Classname JdbcTypeEnum
 * @Description mybatis中jdbc类型转换
 * @Date 2022/5/20 10:18
 */
@AllArgsConstructor
@Getter
public enum JdbcTypeEnum {
    // 数据库中的VARCHAR类型
    STRING("VARCHAR2", "VARCHAR", "java.lang.String"),
    // 数据库中的DATE类型
    DATE("DATE", "DATE", "java.util.Date"),
    NUMBER("NUMBER", "NUMERIC", "java.math.BigDecimal"),
    // 数据库中的TIMESTAMP类型
    TIMESTAMP("TIMESTAMP", "TIMESTAMP", "java.util.Date");


    /**
     * 数据库字段类型
     */
    private String columnType;
    /**
     * 类属性类型
     */
    private String jdbcType;
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
    public static JdbcTypeEnum getByColumnType(String columnType) {
        JdbcTypeEnum[] jdbcTypeEnums = JdbcTypeEnum.values();
        for (JdbcTypeEnum jdbcTypeEnum : jdbcTypeEnums) {
            if (jdbcTypeEnum.getColumnType().equalsIgnoreCase(columnType)) {
                return jdbcTypeEnum;
            }
        }
        return null;
    }

    /**
     * 根据jdbc类型查询枚举类
     *
     * @param jdbcType
     * @return
     */
    public static JdbcTypeEnum getByJdbcType(String jdbcType) {
        JdbcTypeEnum[] jdbcTypeEnums = JdbcTypeEnum.values();
        for (JdbcTypeEnum jdbcTypeEnum : jdbcTypeEnums) {
            if (jdbcTypeEnum.getJdbcType().equalsIgnoreCase(jdbcType)) {
                return jdbcTypeEnum;
            }
        }
        return null;
    }
}
