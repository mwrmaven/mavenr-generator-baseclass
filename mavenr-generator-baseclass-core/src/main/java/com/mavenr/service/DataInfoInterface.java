package com.mavenr.service;

import com.mavenr.entity.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @author mavenr
 * @Classname DataInfoInterface
 * @Description 数据信息接口
 * @Date 2021/12/20 17:04
 */
public interface DataInfoInterface {

    /**
     * 初始化信息
     * @param properties
     */
    void init(Properties properties);

    /**
     * 表信息获取
     * @param properties 参数信息
     * @return
     */
    List<Table> columns(Properties properties) throws Exception;
}
