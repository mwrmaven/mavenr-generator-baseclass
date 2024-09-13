package com.mavenr.entity;

import lombok.*;

/**
 * @author mavenr
 * @Classname Column
 * @Description 字段信息实体类
 * @Date 2020/10/16 11:32 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Column{
    private String columnName;
    private String columnNameCn;
    private String columnType;
    private String propertyName;
    private String propertyType;
    private String jdbcType;
    // 是否主键
    private boolean primaryKey;
    // 字段排序
    private Integer index;
    // 原始字段类型和长度
    private String originalColumnType;
}
