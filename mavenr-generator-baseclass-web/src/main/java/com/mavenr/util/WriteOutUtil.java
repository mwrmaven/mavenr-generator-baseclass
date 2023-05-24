package com.mavenr.util;

import com.mavenr.entity.*;
import com.mavenr.generator.impl.*;
import com.mavenr.service.OutputInterface;
import com.mavenr.systemenum.Charset;

import java.io.*;
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
            String dbName = baseConfig.getName();

            if (columns == null || columns.size() == 0) {
                System.out.println(tableName + "表中字段为空");
            } else {
                GeneratorConfig generatorConfig = GeneratorConfig.builder()
                        .packagePath(packagePath)
                        .tableName(tableName)
                        .tableNameCn(tableNameCn)
                        .dbName(dbName)
                        .columnList(columns)
                        .owner(item.getOwner())
                        .build();
                if (item.getOwner() == null || "".equals(item.getOwner().trim())) {
                    generatorConfig.setOwner(dbName);
                }

                try {
                    BufferedReader br = null;

                    String entityPath = baseConfig.getEntityPath();
                    if (entityPath != null) {
                        InputStream is;
                        if ("".equals(entityPath)) {
                            is = ClassLoader.getSystemResourceAsStream("template/Entity.java");
                        } else {
                            is = new FileInputStream(entityPath);
                        }
                        br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                        generatorConfig.setBr(br);
                        ClassInfo entity = new EntityGenerator().create(generatorConfig);
                        outputInterface.push(entity.getCode(), entity.getFileName(), outPath);
                        is.close();
                    }

                    String voPath = baseConfig.getVoPath();
                    if (voPath != null) {
                        InputStream is;
                        if ("".equals(voPath)) {
                            is = ClassLoader.getSystemResourceAsStream("template/VO.java");
                        } else {
                            is = new FileInputStream(voPath);
                        }
                        br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                        generatorConfig.setBr(br);
                        ClassInfo vo = new VOGenerator().create(generatorConfig);
                        outputInterface.push(vo.getCode(), vo.getFileName(), outPath);
                        is.close();
                    }

                    String boPath = baseConfig.getBoPath();
                    if (boPath != null) {
                        InputStream is;
                        if ("".equals(boPath)) {
                            is = ClassLoader.getSystemResourceAsStream("template/BO.java");
                        } else {
                            is = new FileInputStream(boPath);
                        }
                        br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                        generatorConfig.setBr(br);
                        ClassInfo bo = new BOGenerator().create(generatorConfig);
                        outputInterface.push(bo.getCode(), bo.getFileName(), outPath);

                    }

                    String mapperPath = baseConfig.getMapperPath();
                    if (mapperPath != null) {
                        InputStream is;
                        if ("".equals(mapperPath)) {
                            is = ClassLoader.getSystemResourceAsStream("template/Mapper.java");
                        } else {
                            is = new FileInputStream(mapperPath);
                        }
                        br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                        generatorConfig.setBr(br);
                        ClassInfo mapper = new MapperGenerator().create(generatorConfig);
                        outputInterface.push(mapper.getCode(), mapper.getFileName(), outPath);
                        is.close();
                    }

                    String mapperXmlPath = baseConfig.getMapperXmlPath();
                    if (mapperXmlPath != null) {
                        InputStream is;
                        if ("".equals(mapperXmlPath)) {
                            is = ClassLoader.getSystemResourceAsStream("template/Mapper.xml");
                        } else {
                            is = new FileInputStream(mapperXmlPath);
                        }
                        br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                        generatorConfig.setBr(br);
                        ClassInfo mapperXml = new MapperXmlGenerator().create(generatorConfig);
                        outputInterface.push(mapperXml.getCode(), mapperXml.getFileName(), outPath);
                        is.close();
                    }

                    String servicePath = baseConfig.getServicePath();
                    if (servicePath != null) {
                        InputStream is;
                        if ("".equals(servicePath)) {
                            is = ClassLoader.getSystemResourceAsStream("template/Service.java");
                        } else {
                            is = new FileInputStream(servicePath);
                        }
                        br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                        generatorConfig.setBr(br);
                        ClassInfo service = new ServiceGenerator().create(generatorConfig);
                        outputInterface.push(service.getCode(), service.getFileName(), outPath);
                        is.close();
                    }

                    String serviceImplPath = baseConfig.getServiceImplPath();
                    if (serviceImplPath != null) {
                        InputStream is;
                        if ("".equals(serviceImplPath)) {
                            is = ClassLoader.getSystemResourceAsStream("template/ServiceImpl.java");
                        } else {
                            is = new FileInputStream(serviceImplPath);
                        }
                        br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                        generatorConfig.setBr(br);
                        ClassInfo serviceImpl = new ServiceImplGenerator().create(generatorConfig);
                        outputInterface.push(serviceImpl.getCode(), serviceImpl.getFileName(), outPath);
                        is.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
