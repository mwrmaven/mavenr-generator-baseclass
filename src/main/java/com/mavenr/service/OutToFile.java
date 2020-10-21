package com.mavenr.service;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @author mavenr
 * @Classname OutToFile
 * @Description 将内容输出到文件中
 * @Date 2020/10/17 11:12 下午
 */
public class OutToFile implements OutputInterface {
    @Override
    public void push(String data, String fileName, String path) throws IOException {

        File file = new File(path);
        if (!file.exists() || file.isFile()) {
            file.mkdirs();
        }

        // 文件的路径
        String filePath = path + fileName;

        FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes("UTF-8"));
        channel.write(buffer);
        channel.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
