package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.enums.JdbcTypeEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.CodeCreateUtil;
import com.mavenr.util.TransferUtil;

import java.io.BufferedReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mavenr
 * @Classname MysqlMapperXmlGenerator
 * @Description mysql mapper xml 文件信息生成器
 * @Date 2021/12/22 11:28
 */
public class MapperXmlGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) {
        return CodeCreateUtil.createMapperXml(generatorConfig);
    }
}
