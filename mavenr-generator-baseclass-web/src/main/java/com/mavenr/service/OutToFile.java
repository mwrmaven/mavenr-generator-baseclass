package com.mavenr.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author mavenr
 * @Classname OutToFile
 * @Description 将内容输出到文件中
 * @Date 2020/10/17 11:12 下午
 */
public class OutToFile implements OutputInterface {
    @Override
    public void push(String data, String fileName, String path) throws IOException {
        // 如果没有输出路径，默认为当前路径
        if (path == null || "".equals(path)) {
            path = "." + File.separator;
        }
        File file = new File(path);
        if (!file.exists() || file.isFile()) {
            file.mkdirs();
        }

        // 文件的路径
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
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
