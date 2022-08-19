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
    String type;
    String path;
    String port;
    String name;
    String user;
    String pwd;
    String tableNames;
    String packageBasePath;
    String entityPath;
    String voPath;
    String mapperPath;
    String servicePath;
    String boPath;
    String outPath;
}
