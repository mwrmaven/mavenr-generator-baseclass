package com.mavenr.systemenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mavenr
 * @Classname Charset
 * @Description 编码格式枚举类
 * @Date 2022/9/2 14:05
 */
@AllArgsConstructor
@Getter
public enum Charset {
    UTF_8("UTF-8");
    private String type;
}
