package com.mavenr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.BufferedReader;
import java.util.List;

/**
 * @author mavenr
 * @Classname GeneratorConfig
 * @Description 生成代码时使用的配置
 * @Date 2022/8/19 9:49
 */
@AllArgsConstructor
@ToString
@Builder
@Data
public class GeneratorConfig {

    private BufferedReader br;
    private String packagePath;
    private String tableName;
    private String tableNameCn;
    private String dbName;
    private List<Column> columnList;
}
