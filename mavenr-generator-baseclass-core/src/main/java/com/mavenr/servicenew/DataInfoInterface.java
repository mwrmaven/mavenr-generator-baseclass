package com.mavenr.servicenew;

import com.mavenr.entity.Table;

import java.util.List;

/**
 * @author mavenr
 * @Classname DataInfoInterface
 * @Description 数据信息接口
 * @Date 2021/12/20 17:04
 */
public interface DataInfoInterface {
    /**
     * 表信息获取
     * @return
     */
    public List<Table> columns();
}
