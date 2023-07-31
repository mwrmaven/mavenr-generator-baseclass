package com.mavenr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author mavenr
 * @Classname TemplateInfo
 * @Description TODO
 * @Date 2023/7/29 9:37
 */
@AllArgsConstructor
@ToString
@Builder
@Data
public class TemplateInfo {

    /**
     * 是否勾选生成
     */
    private boolean selected;

    /**
     * 过滤字段
     */
    private String filterStr;

    /**
     * 类末尾追加文本
     */
    private String appendStr;

    /**
     * 模板文件路径
     */
    private String filePath;

    /**
     * 包路径
     */
    private String packagePath;


}
