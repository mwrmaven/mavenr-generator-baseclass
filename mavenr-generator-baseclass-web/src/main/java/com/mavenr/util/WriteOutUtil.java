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
                    String entityPath = baseConfig.getEntityPath();
                    if (entityPath != null) {
                        BufferedReader br = null;
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
                        BufferedReader br = null;
                        if ("".equals(voPath)) {
                            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("template/VO.java")));
                        } else {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream(voPath)));
                        }
                        generatorConfig.setBr(br);
                        ClassInfo vo = new VOGenerator().create(generatorConfig);
                        outputInterface.push(vo.getCode(), vo.getFileName(), outPath);
                    }


//                    ClassInfo mapper = new MapperGenerator().create(generatorConfig);
//                    outputInterface.push(mapper.getCode(), mapper.getFileName(), outPath);
//
//                    ClassInfo mapperXml = null;
//                    if (DatabaseTypeEnum.ORACLE.getType().equalsIgnoreCase(type)) {
//                        mapperXml = new OracleMapperXmlGenerator().create(generatorConfig);
//                    } else if (DatabaseTypeEnum.MYSQL.getType().equalsIgnoreCase(type)) {
//                        mapperXml = new MysqlMapperXmlGenerator().create(generatorConfig);
//                    }
//                    outputInterface.push(mapperXml.getCode(), mapperXml.getFileName(), outPath);
//
//                    ClassInfo service = new ServiceGenerator().create(generatorConfig);
//                    outputInterface.push(service.getCode(), service.getFileName(), outPath);
//
//                    ClassInfo serviceImpl = new ServiceImplGenerator().create(generatorConfig);
//                    outputInterface.push(serviceImpl.getCode(), serviceImpl.getFileName(), outPath);
//
//                    ClassInfo importBo = new ImportBOGenerator().create(generatorConfig);
//                    outputInterface.push(importBo.getCode(), importBo.getFileName(), outPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
