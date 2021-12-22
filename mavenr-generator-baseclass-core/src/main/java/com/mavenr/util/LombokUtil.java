package com.mavenr.util;

/**
 * @author mavenr
 * @Classname LombokUtil
 * @Description 添加lombok注释的工具类
 * @Date 2021/12/22 10:45
 */
public class LombokUtil {

    /**
     * 添加导入包信息
     * @return
     */
    public static String importInfo() {
        String append = "import lombok.*;\n";
        return append;
    }

    /**
     * 添加注解信息
     * @return
     */
    public static String annotationInfo(){
        String append = "@Data\n" +
                "@Builder\n" +
                "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n" +
                "@ToString\n";
        return append;
    }
}
