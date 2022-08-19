package com.mavenr.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author mavenr
 * @Classname FileOptions
 * @Description 文件操作类
 * @Date 2022/6/22 0:02
 */
public class FileOptions {

    public static final int BUFFER_SIZE = 10 * 1024;

    /**
     * 删除文件、文件夹以及文件夹中的子项
     * @param file 文件或文件夹
     */
    public static void deleteFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        try {
            Path path = file.toPath();
            if (file.isFile()) {
                Files.deleteIfExists(path);
                return;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length != 0) {
                    for (File f : files) {
                        deleteFile(f);
                    }
                }
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将流写出到文件
     * @param in
     * @param outFilePath
     */
    public static void writeToFile(InputStream in, String outFilePath) {
        byte[] buffer = new byte[BUFFER_SIZE];
        OutputStream out = null;
        try {
            out = new FileOutputStream(outFilePath);
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
