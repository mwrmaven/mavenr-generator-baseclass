package com.mavenr.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassInfo {

    /**
     * 文件名（包含后缀）
     */
    private String fileName;

    /**
     * 类路径（包含类名）
     */
    private String classPath;

    /**
     * 代码
     */
    private String code;

    /**
     * 类名（包含类前和类后的追加文本）
     */
    private String className;

    /**
     * 直接根据表名转换的类名
     */
    private String originClassName;
}
