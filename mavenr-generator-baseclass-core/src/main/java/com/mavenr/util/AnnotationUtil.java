package com.mavenr.util;

/**
 * @author mavenr
 * @Classname AnnotationUtil
 * @Description 注解信息操作类
 * @Date 2021/12/22 11:31
 */
public class AnnotationUtil {

    /**
     * 类上的注解信息生成
     * @param cn
     * @param param
     * @return
     */
    public static StringBuilder functionComment(String cn, String param) {
        StringBuilder sb = new StringBuilder();
        sb.append("    /**\n");
        sb.append("     * " + cn + "\n");
        sb.append("     * @param " + param + "\n");
        sb.append("     * @return\n");
        sb.append("     */\n");
        return sb;
    }
}
