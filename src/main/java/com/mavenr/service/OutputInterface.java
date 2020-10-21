package com.mavenr.service;

import java.io.IOException;

/**
 * @author mavenr
 * @Classname OutputInterface
 * @Description 输出接口
 * @Date 2020/10/17 11:08 下午
 */
public interface OutputInterface {

    /**
     * 输出内容到指定地方
     *
     * @param data
     * @param fileName
     * @param path
     */
    void push(String data, String fileName, String path) throws IOException;
}
