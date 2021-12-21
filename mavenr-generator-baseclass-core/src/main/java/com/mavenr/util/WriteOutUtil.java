package com.mavenr.util;

import com.mavenr.bo.TableBO;
import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.DatabaseTypeEnum;
import com.mavenr.generator.ClassGenerator;
import com.mavenr.service.OutputInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

/**
 * @author mavenr
 * @Classname WriteOutUtil
 * @Description 输出代码的工具类
 * @Date 2020/10/18 12:16 上午
 */
@Slf4j
public class WriteOutUtil {
    /**
     * 遍历输出类信息和类代码
     * @param tableList 表集合
     * @param outputInterface 输出接口
     * @param properties 参数信息
     */
    public static void write(List<Table> tableList, OutputInterface outputInterface, Properties properties) {
        ClassGenerator classGenerator = new ClassGenerator();
        // 包路径
        String packagePath = properties.getProperty("database.packagePath");
        // 文件输出路径
        String outPath = properties.getProperty("database.outPath");
        // 是否添加lombok
        boolean lombok = "TRUE".equalsIgnoreCase(properties.getProperty("useLombok"));
        // 是否添加swagger
        boolean swagger = "TRUE".equalsIgnoreCase(properties.getProperty("useSwagger"));
        String typeTemp = "";
        // 数据库驱动
        String driverClassName = properties.getProperty("database.driverClassName");
        if (driverClassName.toUpperCase().contains(DatabaseTypeEnum.ORACLE.getType())) {
            typeTemp = DatabaseTypeEnum.ORACLE.getType();
        } else if (driverClassName.toUpperCase().contains(DatabaseTypeEnum.MYSQL.getType())) {
            typeTemp = DatabaseTypeEnum.MYSQL.getType();
        } else {
            return;
        }

        // 数据库类型
        String type = typeTemp;
        tableList.forEach(item -> {
            log.debug(item.toString());
            String tableName = item.getTableName();
            String tableNameCn = item.getTableNameCn();
            List<Column> columns = item.getColumns();

            if (columns == null || columns.size() == 0) {
                log.error(tableName + "表中字段为空");
            } else {
                try {
                    ClassInfo entity = classGenerator.createEntity(packagePath, tableName, tableNameCn, columns, lombok, swagger);
                    outputInterface.push(entity.getCode(), entity.getFileName(), outPath);

                    ClassInfo vo = classGenerator.createVO(packagePath, tableName, tableNameCn, columns, lombok, swagger);
                    outputInterface.push(vo.getCode(), vo.getFileName(), outPath);

                    ClassInfo mapper = classGenerator.createMapper(packagePath, tableName);
                    outputInterface.push(mapper.getCode(), mapper.getFileName(), outPath);

                    ClassInfo mapperXml = null;
                    if (DatabaseTypeEnum.ORACLE.getType().equalsIgnoreCase(type)) {
                        mapperXml = classGenerator.createOracleMapperXml(packagePath, tableName, columns);
                    } else if (DatabaseTypeEnum.MYSQL.getType().equalsIgnoreCase(type)) {
                        mapperXml = classGenerator.createMysqlMapperXml(packagePath, tableName, columns);
                    }

                    outputInterface.push(mapperXml.getCode(), mapperXml.getFileName(), outPath);

                    ClassInfo service = classGenerator.createService(packagePath, tableName);
                    outputInterface.push(service.getCode(), service.getFileName(), outPath);

                    ClassInfo serviceImpl = classGenerator.createServiceImpl(packagePath, tableName, columns);
                    outputInterface.push(serviceImpl.getCode(), serviceImpl.getFileName(), outPath);

                    ClassInfo importBo = classGenerator.createImportBo(packagePath, tableName, columns);
                    outputInterface.push(importBo.getCode(), importBo.getFileName(), outPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
