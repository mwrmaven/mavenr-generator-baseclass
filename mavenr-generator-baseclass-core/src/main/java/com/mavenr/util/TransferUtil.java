package com.mavenr.util;

/**
 * @author mavenr
 * @Classname TransferUtil
 * @Description 转换工具类
 * @Date 2020/10/16 11:37 下午
 */
public class TransferUtil {

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
}
