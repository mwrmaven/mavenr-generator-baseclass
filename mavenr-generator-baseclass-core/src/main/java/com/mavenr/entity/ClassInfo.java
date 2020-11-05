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
     * 代码
     */
    private String code;
}
