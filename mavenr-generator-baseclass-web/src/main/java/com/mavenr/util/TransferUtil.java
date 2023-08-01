package com.mavenr.util;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.systemenum.ClassTypeEnum;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author mavenr
 * @Classname TransferUtil
 * @Description 转换工具类
 * @Date 2020/10/16 11:37 下午
 */
public class TransferUtil {

    /**
     * 匹配代码行中的参数的正则表达式
     */
    private static final Pattern pattern = Pattern.compile("\\$\\{\\w*\\}");

    private static final Pattern columnIndexPattern = Pattern.compile("\\$\\{columnIndex[\\+\\-\\*\\/][0-9]+([.]{1}[0-9]+){0,1}\\}");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将表名转换为基础类名
     *
     * @param tableName 表名
     * @return
     */
    public static String toClassBaseName(String tableName) {
        tableName = toPropertyName(tableName);
        tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
        return tableName;
    }

    /**
     * 将数据库字段转换为类属性名
     *
     * @param columnName 字段名
     * @return
     */
    public static String toPropertyName(String columnName) {
        String propertyName = columnName.toLowerCase();
        int index = propertyName.indexOf("_");
        while (index != -1) {
            if (index == propertyName.length() - 1) {
                propertyName = propertyName.substring(0, propertyName.length() - 1);
                return propertyName;
            } else if (index == 0){
                propertyName = propertyName.substring(1);
            } else {
                propertyName = propertyName.substring(0, index)
                        + propertyName.substring(index + 1, index + 2).toUpperCase()
                        + propertyName.substring(index + 2);
                index = propertyName.indexOf("_");
            }
        }
        return propertyName;
    }

    /**
     * 替换行中的参数
     * @param classType
     * @param line
     * @param generatorConfig
     * @param column
     * @return
     */
    public static String replaceParamToValue(String classType, String line, GeneratorConfig generatorConfig, ClassInfo classInfo, Column column) {
        String result = line;
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            String param = matcher.group();
            switch (param) {
                case "${packagePath}":
                    result = result.replace("${packagePath}", generatorConfig.getPackagePath());
                    break;
                case "${tableName}":
                    result = result.replace("${tableName}", generatorConfig.getTableName());
                    break;
                case "${tableNameCn}":
                    result = result.replace("${tableNameCn}", generatorConfig.getTableNameCn());
                    break;
                case "${className}":
                    result = result.replace("${className}", classInfo.getClassName());
                    break;
                case "${originClassName}":
                    result = result.replace("${originClassName}", classInfo.getOriginClassName());
                    break;
                case "${columnComments}":
                    result = result.replace("${columnComments}", column.getColumnNameCn());
                    break;
                case "${columnName}":
                    result = result.replace("${columnName}", column.getColumnName());
                    break;
                case "${columnType}":
                    result = result.replace("${columnType}", column.getColumnType());
                    break;
                case "${jdbcType}":
                    result = result.replace("${jdbcType}", column.getJdbcType());
                    break;
                case "${createTime}":
                    result = result.replace("${createTime}", sdf.format(new Date()));
                    break;
                case "${propertyName}":
                    result = result.replace("${propertyName}", column.getPropertyName());
                    break;
                case "${propertyType}":
                    result = result.replace("${propertyType}", column.getPropertyType());
                    break;
                case "${entityClassName}":
                    result = result.replace("${entityClassName}", TransferUtil.toClassBaseName(generatorConfig.getTableName()));
                    break;
                case "${entityClassPropertyName}":
                    result = result.replace("${entityClassPropertyName}", TransferUtil.toPropertyName(generatorConfig.getTableName()));
                    break;
                case "${mapperClassName}":
                    result = result.replace("${mapperClassName}", TransferUtil.toClassBaseName(generatorConfig.getTableName()) + ClassTypeEnum.MAPPER.getClassType());
                    break;
                case "${mapperClassPropertyName}":
                    result = result.replace("${mapperClassPropertyName}", TransferUtil.toPropertyName(generatorConfig.getTableName()) + ClassTypeEnum.MAPPER.getClassType());
                    break;
                case "${serviceClassName}":
                    result = result.replace("${serviceClassName}", TransferUtil.toClassBaseName(generatorConfig.getTableName()) + ClassTypeEnum.SERVICE.getClassType());
                    break;
                case "${columns}":
                    result = result.replace("${columns}", generatorConfig.getColumnList().stream().map(Column::getColumnName).collect(Collectors.joining(", ")));
                    break;
                case "${columnIndex}":
                    result = result.replace("${columnIndex}", String.valueOf(column.getIndex()));
                    break;
                default:
            }
        }

        // 匹配字段位置的计算
        Matcher matcher1 = columnIndexPattern.matcher(result);
        while (matcher1.find()) {
            String param = matcher1.group();
            System.out.println("匹配字段位置的计算：" + param);
            String temp = param.substring(2, param.length() - 1);
            // 替换 columnIndex 为下标值
            temp = temp.replace("columnIndex", String.valueOf(column.getIndex()));
            System.out.println("替换字段下标：" + temp);
            // 计算下标值
            JexlBuilder jexlBuilder = new JexlBuilder();
            JexlEngine jexlEngine = jexlBuilder.create();
            JexlExpression expression = jexlEngine.createExpression(temp);
            MapContext mapContext = new MapContext();
            Object evaluate = expression.evaluate(mapContext);
            System.out.println("计算后的结果：" + evaluate.toString());
            // 替换
            result = result.replace(param, evaluate.toString());
        }

        return result;
    }

    /**
     * mapperXml文件替换行中的参数
     * @param line
     * @param generatorConfig
     * @return
     */
    public static String replaceMapperXmlParamToValue(String line, GeneratorConfig generatorConfig) {
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
            if ("${packagePath}".equals(param) || "${tableName}".equals(param)
                    || "${columns}".equals(param) || line.trim().startsWith("WHERE ")
                    || "${primaryKeyType}".equals(param) || "${dbName}".equals(param)
                    || "${owner}".equals(param) || "${mapperClassPath}".equals(param)
                    || "${entityClassPath}".equals(param)) {
                matcher = pattern.matcher(result);
                while (matcher.find()) {
                    param = matcher.group();
                    // 替换参数一次
                    switch (param) {
                        case "${tableName}":
                            result = result.replace("${tableName}", generatorConfig.getTableName());
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
                        case "${entityClassPath}":
                            result = result.replace("${entityClassPath}", generatorConfig.getEntityClassPath());
                            break;
                        case "${mapperClassPath}":
                            result = result.replace("${mapperClassPath}", generatorConfig.getMapperClassPath());
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
