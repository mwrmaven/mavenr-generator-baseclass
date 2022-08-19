package com.mavenr.util;

import com.mavenr.entity.Column;
import com.mavenr.entity.GeneratorConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static String replaceParamToValue(String classType, String line, GeneratorConfig generatorConfig, Column column) {
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
                case "${className}":
                    result = result.replace("${className}", TransferUtil.toClassBaseName(generatorConfig.getTableName()) + classType);
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
                case "${createTime}":
                    result = result.replace("${createTime}", sdf.format(new Date()));
                    break;
                default:
            }
        }
        return result;
    }
}
