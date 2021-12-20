package com.mavenr.servicenew;

import com.mavenr.entity.Table;

import java.io.FileInputStream;
import java.util.List;

/**
 * @author mavenr
 * @Classname FileInterface
 * @Description 文件类型（包含数据库建表语句的文件，需符合建表语句规范）
 * @Date 2021/12/20 17:23
 */
public class FileDataInfo implements DataInfoInterface{


    @Override
    public List<Table> columns(List<String> tableNames) {
        return null;
    }
}
