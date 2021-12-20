package com.mavenr.servicenew;


/**
 * @author mavenr
 * @Classname DatabaseInterface
 * @Description 数据库接口
 * @Date 2021/12/20 16:47
 */
public interface DatabaseInterface extends DataInfoInterface {

    /**
     * 数据库连接
     * @param configFilePath 配置文件路径
     * @return
     */
    public void init(String configFilePath);

    /**
     * 关闭数据库连接
     */
    public void close();
}
