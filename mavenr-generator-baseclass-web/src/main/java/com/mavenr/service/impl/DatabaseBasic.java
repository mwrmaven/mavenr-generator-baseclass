package com.mavenr.service.impl;

import com.mavenr.entity.BaseConfig;
import com.mavenr.service.DataInfoInterface;
import com.mavenr.util.ConnectionUtil;

import java.sql.Connection;

/**
 * @author mavenr
 * @Classname DatabaseBasic
 * @Description 数据库基础类，实现close方法
 * @Date 2021/12/20 17:14
 */
public abstract class DatabaseBasic implements DataInfoInterface {

    /**
     * 数据库连接
     */
    public Connection connection;

    @Override
    public void init(BaseConfig baseConfig) throws Exception {
        connection = ConnectionUtil.getConnectionNew(baseConfig);
    }

    /**
     * 实现数据库连接的关闭
     */
    public void close() throws Exception {
        ConnectionUtil.close(connection);
    }
}
