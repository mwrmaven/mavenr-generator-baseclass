package com.mavenr.util;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.enums.ClassTypeEnum;

import java.io.BufferedReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author mavenr
 * @Classname CodeCreateUtil
 * @Description TODO
 * @Date 2022/8/22 10:01
 */
public class CodeCreateUtil {

    /**
     * 匹配代码行中的参数的正则表达式
     */
    private static final Pattern pattern = Pattern.compile("\\$\\{\\w*\\}");

    public static ClassInfo createEntity(GeneratorConfig generatorConfig, String classType) {
        String tableName = generatorConfig.getTableName();
        String classBaseName = TransferUtil.toClassBaseName(tableName);

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        int i = 0;
        boolean flag = false;
        StringBuilder temp = new StringBuilder();
        try {
            // 遍历查询行中是否存在参数
            while ((line = br.readLine()) != null) {
                i++;
                if (i == 1 && !line.startsWith("package ")) {
                    continue;
                }
                if (flag) {
                    temp.append(line).append("\n");
                } else {
                    // 替换行代码中的参数
                    line = TransferUtil.replaceParamToValue(classType, line, generatorConfig, null);
                    code.append(line).append("\n");
                    if (line.startsWith("public class")) {
                        flag = true;
                    }
                }
            }

            // 处理字段属性值（删除末尾的大括号 }）
            String columnCode = temp.substring(0, temp.lastIndexOf("}"));
            // 遍历字段
            List<Column> columnList = generatorConfig.getColumnList();
            columnList.sort(new Comparator<Column>() {
                @Override
                public int compare(Column o1, Column o2) {
                    return o1.getIndex() - o2.getIndex();
                }
            });
            int idPreIndex = -1;
            for(Column item : columnList) {
                // 导入类跳过系统字段
                String ignore = "ETL_DT,ID,DATA_STATUS,VERSION,CREATE_TIME,CREATOR,REPORT_INSTANCE_ID,TASK_INSTANCE_ID";
                String[] is = ignore.split(",");
                List<String> isList = Arrays.asList(is);
                if (isList.contains(item.getColumnName())) {
                    continue;
                }
                // 替换行代码中的参数
                System.out.println("替换的字段信息为：" + item.toString());
                String result = TransferUtil.replaceParamToValue(classType, columnCode, generatorConfig, item);
                idPreIndex = item.getIndex();
                code.append(result);
            }

            // 在导入类文件中追加
            String endAppend = "    @SubmitReportColumn(fieldName = \"ID\", fieldChineseName = \"主键id\", fieldType = FieldType.VARCHAR, isBizPrimaryKey = true, fieldValueWay = FieldValueWay.SYSTEM, systemParaName = SystemParaConstant.NULL_INIT_UUID, columnIndex = " + (idPreIndex + 1 + 1) + ")\n" +
                    "    private String id;\n" +
                    "\n" +
                    "    @ExcelIgnore\n" +
                    "    @SubmitReportColumn(fieldName = \"DATA_STATUS\", fieldChineseName = \"数据有效状态\", fieldType = FieldType.NUMBER, isDataField = false, fieldValueWay = FieldValueWay.UPDATEDATA, updatePre = \"0\", data = \"1\")\n" +
                    "    String dataStatus;\n" +
                    "\n" +
                    "    @ExcelIgnore\n" +
                    "    @SubmitReportColumn(fieldName = \"VERSION\", fieldChineseName = \"版本\", fieldType = FieldType.NUMBER, isDataField = false, fieldValueWay = FieldValueWay.SYSTEM, systemParaName = SystemParaConstant.VERSION, insertVersion = true)\n" +
                    "    String version;\n" +
                    "\n" +
                    "    @ExcelIgnore\n" +
                    "    @SubmitReportColumn(fieldName = \"CREATE_TIME\", fieldChineseName = \"创建时间\", isDataField = false, isRecordOpeTime = true, fieldType = FieldType.TIMESTAMP, fieldValueWay = FieldValueWay.SYSTEM, systemParaName = SystemParaConstant.TIME_TIMESTAMP, isCreateTime = true)\n" +
                    "    String createTime;\n" +
                    "\n" +
                    "    @ExcelIgnore\n" +
                    "    @SubmitReportColumn(fieldName = \"CREATOR\", fieldChineseName = \"创建人\", isDataField = false, fieldType = FieldType.VARCHAR, fieldValueWay = FieldValueWay.SYSTEM, systemParaName = SystemParaConstant.USER_CODE)\n" +
                    "    String creator;\n" +
                    "\n" +
                    "    @ExcelIgnore\n" +
                    "    @SubmitReportColumn(fieldName = \"REPORT_INSTANCE_ID\", fieldChineseName = \"报表实例ID\", fieldType = FieldType.VARCHAR, isDataField = false, fieldValueWay = FieldValueWay.SYSTEM, systemParaName = SystemParaConstant.REPORT_INSTANCE_ID)\n" +
                    "    String reportInstanceId;\n" +
                    "\n" +
                    "    @ExcelIgnore\n" +
                    "    @SubmitReportColumn(fieldName = \"TASK_INSTANCE_ID\", fieldChineseName = \"任务实例ID\", fieldType = FieldType.VARCHAR, isDataField = false, fieldValueWay = FieldValueWay.SYSTEM, systemParaName = SystemParaConstant.TASK_INSTANCE_ID)\n" +
                    "    String taskInstanceId;\n";

            String exportEndAppend = "    /**\n" +
                    "     * id\n" +
                    "     */\n" +
                    "    @SubmitReportColumn(fieldName = \"ID\", fieldChineseName = \"主键id\", fieldType = FieldType.VARCHAR)\n" +
                    "    private String id;";
            code.append(exportEndAppend);

            code.append("\n}");
        } catch (Exception e) {
            System.out.println("解析模板文件出错！" + e.getMessage());
            e.printStackTrace();
        }

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + classType + ".java")
                .code(code.toString())
                .build();
        return classInfo;
    }

    public static ClassInfo createMapper(GeneratorConfig generatorConfig, String classType) {
        String tableName = generatorConfig.getTableName();
        String classBaseName = TransferUtil.toClassBaseName(tableName);

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        int i = 0;
        StringBuilder temp = new StringBuilder();
        try {
            // 遍历查询行中是否存在参数
            while ((line = br.readLine()) != null) {
                i++;
                if (i == 1 && !line.startsWith("package ")) {
                    continue;
                }
                // 替换行代码中的参数
                line = TransferUtil.replaceParamToValue(classType, line, generatorConfig, null);
                code.append(line).append("\n");
            }
        } catch (Exception e) {
            System.out.println("解析模板文件出错！" + e.getMessage());
            e.printStackTrace();
        }

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + classType + ".java")
                .code(code.toString())
                .build();
        return classInfo;
    }

    public static ClassInfo createMapperXml(GeneratorConfig generatorConfig) {
        String tableName = generatorConfig.getTableName();
        String entityClassName = TransferUtil.toClassBaseName(tableName);
        String mapperClassName = TransferUtil.toClassBaseName(tableName) + ClassTypeEnum.MAPPER.getClassType();
        String packagePath = generatorConfig.getPackagePath();
        List<Column> columnList = generatorConfig.getColumnList();
        String allColumns = columnList.stream().map(Column::getColumnName).collect(Collectors.joining(", "));

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        int i = 0;
        try {
            // 遍历查询行中是否存在参数
            while ((line = br.readLine()) != null) {
                i++;
                if (i == 1 && !line.startsWith("package ")) {
                    continue;
                }
                // 替换行代码中的参数
                line = replaceParamToValue(line, generatorConfig);
                code.append(line).append("\n");
            }
        } catch (Exception e) {
            System.out.println("解析模板文件出错！" + e.getMessage());
            e.printStackTrace();
        }

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(mapperClassName + ".xml")
                .code(code.toString())
                .build();
        return classInfo;
    }

    /**
     * 替换行中的参数
     * @param line
     * @param generatorConfig
     * @return
     */
    private static String replaceParamToValue(String line, GeneratorConfig generatorConfig) {
        String result = line;
        Matcher matcher = pattern.matcher(result);
        List<Column> columnList = generatorConfig.getColumnList();
        columnList.sort(new Comparator<Column>() {
            @Override
            public int compare(Column o1, Column o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });
        List<Column> primaryKeys = columnList.stream().filter(item -> item.isPrimaryKey()).collect(Collectors.toList());
        Column columnPrimaryKey;
        if (primaryKeys.size() == 0) {
            columnPrimaryKey = new Column();
            columnPrimaryKey.setColumnName("ID");
            columnPrimaryKey.setPropertyName("id");
            columnPrimaryKey.setColumnType("VARCHAR");
            columnPrimaryKey.setJdbcType("VARCHAR");
        } else {
            columnPrimaryKey = primaryKeys.get(0);
        }

        StringBuilder sb = new StringBuilder();
        if (matcher.find()) {
            String param = matcher.group();
            if ("${mapperClassName}".equals(param) || "${entityClassName}".equals(param)
                    || "${packagePath}".equals(param) || "${tableName}".equals(param)
                    || "${tableNameCn}".equals(param)
                    || "${columns}".equals(param) || line.trim().startsWith("WHERE ")
                    || "${primaryKeyType}".equals(param) || "${dbName}".equals(param) || "${owner}".equals(param)) {
                matcher = pattern.matcher(result);
                while (matcher.find()) {
                    param = matcher.group();
                    // 替换参数一次
                    switch (param) {
                        case "${tableName}":
                            result = result.replace("${tableName}", generatorConfig.getTableName());
                            break;
                        case "${tableNameCn}":
                            result = result.replace("${tableNameCn}", generatorConfig.getTableNameCn());
                            break;
                        case "${primaryKeyColumn}":
                            result = result.replace("${primaryKeyColumn}", columnPrimaryKey.getColumnName());
                            break;
                        case "${primaryKeyProperty}":
                            result = result.replace("${primaryKeyProperty}", columnPrimaryKey.getPropertyName());
                            break;
                        case "${primaryKeyType}":
                            result = result.replace("${primaryKeyType}", columnPrimaryKey.getColumnType());
                            break;
                        case "${columns}":
                            result = result.replace("${columns}", columnList.stream().map(Column::getColumnName).collect(Collectors.joining(", ")));
                            break;
                        case "${mapperClassName}":
                            result = result.replace("${mapperClassName}", TransferUtil.toClassBaseName(generatorConfig.getTableName()) + ClassTypeEnum.MAPPER.getClassType());
                            break;
                        case "${entityClassName}":
                            result = result.replace("${entityClassName}", TransferUtil.toClassBaseName(generatorConfig.getTableName()));
                            break;
                        case "${packagePath}":
                            result = result.replace("${packagePath}", generatorConfig.getPackagePath());
                            break;
                        case "${dbName}":
                            result = result.replace("${dbName}", generatorConfig.getDbName());
                            break;
                        case "${owner}":
                            result = result.replace("${owner}", generatorConfig.getOwner());
                            break;
                        case "${jdbcType}":
                            result = result.replace("${jdbcType}", columnPrimaryKey.getJdbcType());
                            break;
                        default:
                    }
                }
                sb.append(result);
            } else {
                matcher = pattern.matcher(result);
                List<String> pList = new ArrayList<>();
                while (matcher.find()) {
                    pList.add(matcher.group());
                }
                // 遍历所有表字段
                for (Column column : columnList) {
                    String temp = result;
                    for (String str : pList) {
                        switch (str) {
                            case "${columnName}":
                                temp = temp.replace("${columnName}", column.getColumnName());
                                break;
                            case "${propertyName}":
                                temp = temp.replace("${propertyName}", column.getPropertyName());
                                break;
                            case "${columnType}":
                                temp = temp.replace("${columnType}", column.getColumnType());
                                break;
                            case "${jdbcType}":
                                temp = temp.replace("${jdbcType}", column.getJdbcType());
                                break;
                            case "${comma}":
                                temp = temp.replace("${comma}", ",");
                                break;
                            default:
                        }
                    }
                    sb.append(temp).append("\n");
                }
                if ("${comma}".equals(pList.get(pList.size() - 1))) {
                    sb = new StringBuilder(sb.substring(0, sb.length() - 2)).append("\n");
                }
            }
        } else {
            sb.append(line);
        }
        return sb.toString();
    }

}
