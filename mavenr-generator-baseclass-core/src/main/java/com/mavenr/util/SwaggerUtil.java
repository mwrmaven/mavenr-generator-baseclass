package com.mavenr.util;

/**
 * @author mavenr
 * @Classname SwaggerUtil
 * @Description 添加swagger注释信息的工具类
 * @Date 2021/12/22 11:18
 */
public class SwaggerUtil {
    /**
     * 添加导入包信息
     * @return
     */
    public static String importInfo() {
        String append = "import io.swagger.annotations.*;\n";
        return append;
    }
}
