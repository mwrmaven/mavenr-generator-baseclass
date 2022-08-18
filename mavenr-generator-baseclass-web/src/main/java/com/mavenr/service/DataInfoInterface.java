package com.mavenr.service;

import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.Table;

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
     * @param baseConfig
     */
    void init(BaseConfig baseConfig);

    /**
     * 表信息获取
     * @param baseConfig 参数信息
     * @return
     */
    List<Table> columns(BaseConfig baseConfig) throws Exception;
}
