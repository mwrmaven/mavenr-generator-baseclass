package com.mavenr.util;

import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.DatabaseTypeEnum;
import com.mavenr.generator.impl.*;
import com.mavenr.service.OutputInterface;

import java.io.File;
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
        String outPath = System.getProperty("user.dir") + File.separator + "code";
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
                try {
                    ClassInfo entity = new EntityGenerator().create(packagePath, tableName, tableNameCn, columns);
                    outputInterface.push(entity.getCode(), entity.getFileName(), outPath);

                    ClassInfo vo = new VOGenerator().create(packagePath, tableName, tableNameCn, columns);
                    outputInterface.push(vo.getCode(), vo.getFileName(), outPath);

                    ClassInfo mapper = new MapperGenerator().create(packagePath, tableName, tableNameCn, columns);
                    outputInterface.push(mapper.getCode(), mapper.getFileName(), outPath);

                    ClassInfo mapperXml = null;
                    if (DatabaseTypeEnum.ORACLE.getType().equalsIgnoreCase(type)) {
                        mapperXml = new OracleMapperXmlGenerator().create(packagePath, tableName, tableNameCn, columns);
                    } else if (DatabaseTypeEnum.MYSQL.getType().equalsIgnoreCase(type)) {
                        mapperXml = new MysqlMapperXmlGenerator().create(packagePath, tableName, tableNameCn, columns);
                    }
                    outputInterface.push(mapperXml.getCode(), mapperXml.getFileName(), outPath);

                    ClassInfo service = new ServiceGenerator().create(packagePath, tableName, tableNameCn, columns);
                    outputInterface.push(service.getCode(), service.getFileName(), outPath);

                    ClassInfo serviceImpl = new ServiceImplGenerator().create(packagePath, tableName, tableNameCn, columns);
                    outputInterface.push(serviceImpl.getCode(), serviceImpl.getFileName(), outPath);

                    ClassInfo importBo = new ImportBOGenerator().create(packagePath, tableName, tableNameCn, columns);
                    outputInterface.push(importBo.getCode(), importBo.getFileName(), outPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
