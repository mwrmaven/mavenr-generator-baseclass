package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.systemenum.ClassTypeEnum;
import com.mavenr.util.CodeCreateUtil;

/**
 * @author mavenr
 * @Classname OnlyReplaceGenerator
 * @Description 只替换文件中的数据库参数，不做逻辑处理
 * @Date 2021/12/22 14:23
 */
public class OnlyReplaceGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) throws Exception {
        return CodeCreateUtil.createOnlyReplace(generatorConfig);
    }
}
