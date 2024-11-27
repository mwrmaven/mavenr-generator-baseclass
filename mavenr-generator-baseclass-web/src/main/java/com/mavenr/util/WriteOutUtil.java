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
    public static void write(List<Table> tableList, OutputInterface outputInterface, BaseConfig baseConfig) throws Exception {
        // 文件输出路径
        String outPath = baseConfig.getOutPath();
        File file = new File(outPath);
        if (!file.exists() || file.isFile()) {
            // 创建文件夹
            file.mkdirs();
        }

        // 类名前后追加的内容
        String classNameBefore = baseConfig.getClassNameBefore();
        String classNameAfter = baseConfig.getClassNameAfter();
        // 循环表信息
        for (Table item : tableList) {
            // 遍历表信息
            System.out.println("-------------------------------");
            System.out.println(item.getTableName());
            List<Column> columns1 = item.getColumns();
            for (Column c : columns1) {
                System.out.println(c.getColumnName() + " " + c.getOriginalColumnType() + " " + c.getColumnNameCn());
            }
            System.out.println("-------------------------------");
            System.out.println("开始解析表：" + item.toString());
            String tableName = item.getTableName();
            String tableNameCn = item.getTableNameCn();
            List<Column> columns = columns1;
            String dbName = baseConfig.getName();

            if (columns == null || columns.size() == 0) {
                System.out.println(tableName + "表中字段为空");
            } else {
                GeneratorConfig generatorConfig = GeneratorConfig.builder()
                        .tableName(tableName)
                        .tableNameCn(tableNameCn)
                        .dbName(dbName)
                        .columnList(columns)
                        .owner(item.getOwner())
                        .classNameBefore(classNameBefore)
                        .classNameAfter(classNameAfter)
                        .build();
                if (item.getOwner() == null || "".equals(item.getOwner().trim())) {
                    generatorConfig.setOwner(dbName);
                }

                BufferedReader br = null;

                TemplateInfo entity = baseConfig.getEntity();
                if (entity.isSelected()) {
                    String packagePath = entity.getPackagePath();
                    generatorConfig.setPackagePath(packagePath);
                    String filePath = entity.getFilePath();
                    String filterStr = entity.getFilterStr();
                    String appendStr = entity.getAppendStr();
                    generatorConfig.setFilterStr(filterStr);
                    generatorConfig.setAppendStr(appendStr);
                    InputStream is;
                    if ("".equals(filePath)) {
                        is = ClassLoader.getSystemResourceAsStream("template/Entity.java");
                    } else {
                        is = new FileInputStream(filePath);
                    }
                    br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                    generatorConfig.setBr(br);
                    // 生成类代码
                    ClassInfo entityClassInfo = new EntityGenerator().create(generatorConfig);
                    // 获取类路径，后边其他类中会用到
                    generatorConfig.setEntityClassPath(entityClassInfo.getClassPath());
                    outputInterface.push(entityClassInfo.getCode(), entityClassInfo.getFileName(), outPath);
                    is.close();
                }

                TemplateInfo mapper = baseConfig.getMapper();
                if (mapper.isSelected()) {
                    String packagePath = mapper.getPackagePath();
                    generatorConfig.setPackagePath(packagePath);
                    String filePath = mapper.getFilePath();
                    InputStream is;
                    if ("".equals(filePath)) {
                        is = ClassLoader.getSystemResourceAsStream("template/Mapper.java");
                    } else {
                        is = new FileInputStream(filePath);
                    }
                    br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                    generatorConfig.setBr(br);
                    ClassInfo mapperClassInfo = new MapperGenerator().create(generatorConfig);
                    generatorConfig.setMapperClassPath(mapperClassInfo.getClassPath());
                    outputInterface.push(mapperClassInfo.getCode(), mapperClassInfo.getFileName(), outPath);
                    is.close();
                }

                TemplateInfo mapperXml = baseConfig.getMapperXml();
                if (mapperXml.isSelected()) {
                    String filePath = mapperXml.getFilePath();
                    InputStream is;
                    if ("".equals(filePath)) {
                        is = ClassLoader.getSystemResourceAsStream("template/Mapper.xml");
                    } else {
                        is = new FileInputStream(filePath);
                    }
                    br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                    generatorConfig.setBr(br);
                    ClassInfo mapperXmlClassInfo = new MapperXmlGenerator().create(generatorConfig);
                    outputInterface.push(mapperXmlClassInfo.getCode(), mapperXmlClassInfo.getFileName(), outPath);
                    is.close();
                }

                TemplateInfo service = baseConfig.getService();
                if (service.isSelected()) {
                    String packagePath = service.getPackagePath();
                    generatorConfig.setPackagePath(packagePath);
                    String filePath = service.getFilePath();
                    InputStream is;
                    if ("".equals(filePath)) {
                        is = ClassLoader.getSystemResourceAsStream("template/Service.java");
                    } else {
                        is = new FileInputStream(filePath);
                    }
                    br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                    generatorConfig.setBr(br);
                    ClassInfo serviceClassInfo = new ServiceGenerator().create(generatorConfig);
                    generatorConfig.setServiceClassPath(serviceClassInfo.getClassPath());
                    outputInterface.push(serviceClassInfo.getCode(), serviceClassInfo.getFileName(), outPath);
                    is.close();
                }

                TemplateInfo serviceImpl = baseConfig.getServiceImpl();
                if (serviceImpl.isSelected()) {
                    String packagePath = serviceImpl.getPackagePath();
                    generatorConfig.setPackagePath(packagePath);
                    String filePath = serviceImpl.getFilePath();
                    InputStream is;
                    if ("".equals(filePath)) {
                        is = ClassLoader.getSystemResourceAsStream("template/ServiceImpl.java");
                    } else {
                        is = new FileInputStream(filePath);
                    }
                    br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                    generatorConfig.setBr(br);
                    ClassInfo serviceImplClassInfo = new ServiceImplGenerator().create(generatorConfig);
                    outputInterface.push(serviceImplClassInfo.getCode(), serviceImplClassInfo.getFileName(), outPath);
                    is.close();
                }

                TemplateInfo onlyReplace = baseConfig.getOnlyReplace();
                if (onlyReplace.isSelected()) {
                    // 获取模板路径
                    String filePath = onlyReplace.getFilePath();
                    InputStream is;
                    if ("".equals(filePath)) {
                        is = ClassLoader.getSystemResourceAsStream("template/ServiceImpl.java");
                    } else {
                        is = new FileInputStream(filePath);
                    }
                    br = new BufferedReader(new InputStreamReader(is, Charset.UTF_8.getType()));
                    generatorConfig.setBr(br);
                    ClassInfo onlyReplaceClassInfo = new OnlyReplaceGenerator().create(generatorConfig);
                    outputInterface.push(onlyReplaceClassInfo.getCode(), onlyReplaceClassInfo.getFileName(), outPath);
                    is.close();
                }
            }
        }
    }
}
