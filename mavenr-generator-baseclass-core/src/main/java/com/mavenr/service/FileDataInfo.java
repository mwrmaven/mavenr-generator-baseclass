package com.mavenr.service;

import com.mavenr.entity.Table;

import java.util.List;
import java.util.Properties;

/**
 * @author mavenr
 * @Classname FileInterface
 * @Description 文件类型（包含数据库建表语句的文件，需符合建表语句规范）
 * @Date 2021/12/20 17:23
 */
public class FileDataInfo implements DataInfoInterface{

    @Override
    public void init(Properties properties) {

    }

    @Override
    public List<Table> columns(Properties properties) throws Exception {
        return null;
    }
}
