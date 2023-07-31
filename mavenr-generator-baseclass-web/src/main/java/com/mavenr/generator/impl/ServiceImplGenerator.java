package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.systemenum.ClassTypeEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.CodeCreateUtil;

/**
 * @author mavenr
 * @Classname ServiceImplGenerator
 * @Description service impl 实现类的生成器
 * @Date 2021/12/22 14:23
 */
public class ServiceImplGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) throws Exception {
        return CodeCreateUtil.createServiceImpl(generatorConfig, ClassTypeEnum.SERVICEIMPL.getClassType());
    }
}
