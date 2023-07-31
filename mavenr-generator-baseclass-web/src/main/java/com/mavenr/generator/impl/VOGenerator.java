package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.systemenum.ClassTypeEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.CodeCreateUtil;


/**
 * @author mavenr
 * @Classname VOGenerator
 * @Description vo类信息生成器
 * @Date 2021/12/22 11:16
 */
public class VOGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) throws Exception {
        // 生成 vo 的类代码
        return CodeCreateUtil.createEntity(generatorConfig, ClassTypeEnum.VO.getClassType());
    }
}
