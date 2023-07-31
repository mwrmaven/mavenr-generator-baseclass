package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.CodeCreateUtil;

/**
 * @author mavenr
 * @Classname MysqlMapperXmlGenerator
 * @Description mysql mapper xml 文件信息生成器
 * @Date 2021/12/22 11:28
 */
public class MapperXmlGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) throws Exception {
        return CodeCreateUtil.createMapperXml(generatorConfig);
    }
}
