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
     * 生成类的模板信息
     */
    TemplateInfo entity;
    TemplateInfo mapper;
    TemplateInfo mapperXml;
    TemplateInfo service;
    TemplateInfo serviceImpl;
    /**
     * 文件输出路径
     */
    String outPath;

    /**
     * 类名前追加
     */
    String classNameBefore;

    /**
     * 类名后追加
     */
    String classNameAfter;
}
