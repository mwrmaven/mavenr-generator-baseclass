package com.mavenr.util;

import com.mavenr.entity.*;
import com.mavenr.generator.impl.*;
import com.mavenr.service.OutputInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author mavenr
 * @Classname WriteOutUtil
 * @Description 输出代码的工具类
 * @Date 2020/10/18 12:16 上午
 */
public class WriteOutUtil {
    /**
     * 遍历输出类信息和类代码
     * @param tableList 表集合
     * @param outputInterface 输出接口
     * @param baseConfig 参数信息
     */
    public static void write(List<Table> tableList, OutputInterface outputInterface, BaseConfig baseConfig) {
        // 包路径
        String packagePath = baseConfig.getPackageBasePath();
        // 文件输出路径
        String outPath = baseConfig.getOutPath();
        File file = new File(outPath);
        if (!file.exists() || file.isFile()) {
            // 创建文件夹
            file.mkdirs();
        }

        // 数据库类型
        String type = baseConfig.getType();
        tableList.forEach(item -> {
            System.out.println("开始解析表：" + item.toString());
            String tableName = item.getTableName();
            String tableNameCn = item.getTableNameCn();
            List<Column> columns = item.getColumns();

            if (columns == null || columns.size() == 0) {
                System.out.println(tableName + "表中字段为空");
            } else {
                GeneratorConfig generatorConfig = GeneratorConfig.builder()
                        .packagePath(packagePath)
                        .tableName(tableName)
                        .tableNameCn(tableNameCn)
                        .columnList(columns)
                        .build();

                try {
                    BufferedReader br = null;

                    String entityPath = baseConfig.getEntityPath();
                    if (entityPath != null) {
                        if ("".equals(entityPath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/Entity.java")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(entityPath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo entity = new EntityGenerator().create(generatorConfig);
                        outputInterface.push(entity.getCode(), entity.getFileName(), outPath);
                    }

                    String voPath = baseConfig.getVoPath();
                    if (voPath != null) {
                        if ("".equals(voPath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/VO.java")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(voPath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo vo = new VOGenerator().create(generatorConfig);
                        outputInterface.push(vo.getCode(), vo.getFileName(), outPath);
                    }

                    String boPath = baseConfig.getBoPath();
                    if (boPath != null) {
                        if ("".equals(boPath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/BO.java")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(boPath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo bo = new BOGenerator().create(generatorConfig);
                        outputInterface.push(bo.getCode(), bo.getFileName(), outPath);
                    }

                    String mapperPath = baseConfig.getMapperPath();
                    if (mapperPath != null) {
                        if ("".equals(mapperPath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/Mapper.java")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(mapperPath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo mapper = new MapperGenerator().create(generatorConfig);
                        outputInterface.push(mapper.getCode(), mapper.getFileName(), outPath);
                    }

                    String mapperXmlPath = baseConfig.getMapperXmlPath();
                    if (mapperXmlPath != null) {
                        if ("".equals(mapperXmlPath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/Mapper.xml")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(mapperXmlPath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo mapperXml = new MapperXmlGenerator().create(generatorConfig);
                        outputInterface.push(mapperXml.getCode(), mapperXml.getFileName(), outPath);
                    }

                    String servicePath = baseConfig.getServicePath();
                    if (servicePath != null) {
                        if ("".equals(servicePath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/Service.java")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(servicePath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo service = new ServiceGenerator().create(generatorConfig);
                        outputInterface.push(service.getCode(), service.getFileName(), outPath);
                    }

                    String serviceImplPath = baseConfig.getServiceImplPath();
                    if (serviceImplPath != null) {
                        if ("".equals(serviceImplPath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/ServiceImpl.java")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(serviceImplPath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo serviceImpl = new ServiceImplGenerator().create(generatorConfig);
                        outputInterface.push(serviceImpl.getCode(), serviceImpl.getFileName(), outPath);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
