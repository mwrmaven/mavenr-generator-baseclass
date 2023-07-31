package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.systemenum.ClassTypeEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.CodeCreateUtil;

/**
 * @author mavenr
 * @Classname ServiceGenerator
 * @Description service类信息生成器
 * @Date 2021/12/22 14:22
 */
public class ServiceGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) throws Exception {
        return CodeCreateUtil.createMapper(generatorConfig, ClassTypeEnum.SERVICE.getClassType());
    }
}
