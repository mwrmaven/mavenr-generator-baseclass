package com.mavenr.generator;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.GeneratorConfig;

/**
 * @author mavenr
 * @Classname ClassGeneratorInterface
 * @Description 类生成器接口
 * @Date 2021/12/21 18:00
 */
public interface ClassGeneratorInterface {

    /**
     * 创建表对应的类的信息
     * @param generatorConfig 生成类代码的参数
     * @return
     */
    ClassInfo create(GeneratorConfig generatorConfig);
}
