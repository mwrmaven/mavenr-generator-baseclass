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
     *
     * @param tableBO
     * @param outputInterface
     * @param outPath
     */
    public static void write(String type, TableBO tableBO, OutputInterface outputInterface, String outPath,
                             boolean lombok, boolean swagger) {
        ClassGenerator classGenerator = new ClassGenerator();
        String packagePath = tableBO.getPackagePath();
        List<Table> tableList = tableBO.getTableList();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
