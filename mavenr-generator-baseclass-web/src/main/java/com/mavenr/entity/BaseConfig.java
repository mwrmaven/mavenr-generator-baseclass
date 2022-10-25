package com.mavenr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author mavenr
 * @Classname BaseConfig
 * @Description TODO
 * @Date 2022/8/18 13:40
 */
@AllArgsConstructor
@ToString
@Builder
@Data
public class BaseConfig {
    /**
     * 数据库类型
     */
    String type;
    /**
     * 数据库地址
     */
    String path;
    /**
     * 数据库端口
     */
    String port;
    /**
     * 数据库名称
     */
    String name;
    /**
     * 用户名
     */
    String user;
    /**
     * 密码
     */
    String pwd;
    /**
     * 表明，多个表名以","分隔
     */
    String tableNames;
    /**
     * 包的基础路径
     */
    String packageBasePath;
    String entityPath;
    String voPath;
    String mapperPath;
    String mapperXmlPath;
    String servicePath;
    String serviceImplPath;
    String boPath;
    /**
     * 文件输出路径
     */
    String outPath;
}
