package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.enums.ClassTypeEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.CodeCreateUtil;

/**
 * @author mavenr
 * @Classname EntityGenerator
 * @Description 实体类生成器
 * @Date 2021/12/22 10:57
 */
public class EntityGenerator implements ClassGeneratorInterface {



    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) {
        // 生成 entity 的类代码
        return CodeCreateUtil.createEntity(generatorConfig, ClassTypeEnum.ENTITY.getClassType());
    }


}
