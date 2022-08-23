package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.enums.ClassTypeEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.CodeCreateUtil;

/**
 * @author mavenr
 * @Classname BOGenerator
 * @Description BO类生成器
 * @Date 2022/8/22 10:21
 */
public class BOGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) {
        // 生成 bo 的类代码
        return CodeCreateUtil.createEntity(generatorConfig, ClassTypeEnum.BO.getClassType());
    }
}
