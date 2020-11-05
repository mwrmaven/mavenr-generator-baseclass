package com.mavenr.entity;

import lombok.*;

import java.util.List;

/**
 * @author mavenr
 * @Classname Table
 * @Description 表信息实体类
 * @Date 2020/10/16 11:32 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Table {
    private String tableName;
    private String tableNameCn;
    private List<Column> columns;
}
