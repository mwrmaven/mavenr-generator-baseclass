package com.mavenr.generator;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;

import java.util.List;

/**
 * @author mavenr
 * @Classname ClassGeneratorInterface
 * @Description 类生成器接口
 * @Date 2021/12/21 18:00
 */
public interface ClassGeneratorInterface {

    /**
     * 创建表对应的类的信息
     * @param packagePath 包路径
     * @param tableName 表名
     * @param tableNameCn 表中文名
     * @param columns 表字段信息
     * @return
     */
    ClassInfo create(String packagePath, String tableName, String tableNameCn,
                     List<Column> columns);
}
