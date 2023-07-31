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

    /**
     * 模板文件输入流
     */
    private BufferedReader br;
    /**
     * 文件所在的包路径
     */
    private String packagePath;
    /**
     * 实体类路径
     */
    private String entityClassPath;
    /**
     * 数据库映射类路径
     */
    private String mapperClassPath;
    /**
     * 服务接口类路径
     */
    private String serviceClassPath;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表中文名
     */
    private String tableNameCn;
    /**
     * 数据库名
     */
    private String dbName;
    /**
     * 字段集合
     */
    private List<Column> columnList;
    /**
     * 数据库所属
     */
    private String owner;

    /**
     * 过滤字段
     */
    private String filterStr;

    /**
     * 类末尾追加文本
     */
    private String appendStr;

    /**
     * 类名前追加
     */
    private String classNameBefore;

    /**
     * 类名后追加
     */
    private String classNameAfter;
}
