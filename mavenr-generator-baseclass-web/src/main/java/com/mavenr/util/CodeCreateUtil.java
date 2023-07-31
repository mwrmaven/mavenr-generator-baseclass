package com.mavenr.util;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.systemenum.ClassTypeEnum;

import java.io.BufferedReader;
import java.util.*;
import java.util.prefs.BackingStoreException;
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

    public static ClassInfo createEntity(GeneratorConfig generatorConfig, String classType) throws Exception {
        String tableName = generatorConfig.getTableName();
        String before = generatorConfig.getClassNameBefore();
        String after = generatorConfig.getClassNameAfter();
        String classBaseName = TransferUtil.toClassBaseName(tableName);

        ClassInfo classInfo = ClassInfo.builder().build();
        // 保存根据表名转换的原类名
        classInfo.setOriginClassName(classBaseName);
        if (before != null && !"".equals(before)) {
            classBaseName = TransferUtil.toClassBaseName(before) + classBaseName;
        }
        if (after != null && !"".equals(after)) {
            classBaseName = TransferUtil.toClassBaseName(after) + classBaseName;
        }
        classBaseName += classType;
        // 保存添加前缀、后缀以及类型后的类名
        classInfo.setClassName(classBaseName);
        // 保存文件名
        classInfo.setFileName(classBaseName + ".java");
        // 保存类的完整路径
        String packagePath = generatorConfig.getPackagePath();
        classInfo.setClassPath(packagePath + "." + classBaseName);

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        int i = 0;
        boolean flag = false;
        StringBuilder temp = new StringBuilder();
        // 获取跳过得字段
        String filterStr = generatorConfig.getFilterStr();
        List<String> filterList = null;
        if (filterStr != null && !"".equals(filterStr)) {
            String[] filter = filterStr.split(",");
            filterList = Arrays.asList(filter);
        }

        // 遍历查询行中是否存在参数
        while ((line = br.readLine()) != null) {
            i++;
            if (i == 1 && !line.startsWith("package ")) {
                continue;
            }
            if (flag) {
                // 只添加字段属性行
                temp.append(line).append("\n");
            } else {
                // 替换行代码中的参数
                line = TransferUtil.replaceParamToValue(classType, line, generatorConfig, classInfo, null);
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
        for(Column item : columnList) {
            // 导入类跳过系统字段
            if (filterList != null && filterList.contains(item.getColumnName())) {
                continue;
            }
            // 替换行代码中的参数
            System.out.println("替换的字段信息为：" + item.toString());
            String result = TransferUtil.replaceParamToValue(classType, columnCode, generatorConfig, classInfo, item);
            code.append(result);
        }

        // 追加文本
        String appendStr = generatorConfig.getAppendStr();
        code.append(appendStr);

        code.append("\n}");

        classInfo.setCode(code.toString());
        return classInfo;
    }

    public static ClassInfo createMapper(GeneratorConfig generatorConfig, String classType) throws Exception {
        String tableName = generatorConfig.getTableName();
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String entityClassPath = generatorConfig.getEntityClassPath();
        String before = generatorConfig.getClassNameBefore();
        String after = generatorConfig.getClassNameAfter();

        ClassInfo classInfo = ClassInfo.builder().build();
        // 保存根据表名转换的原类名
        classInfo.setOriginClassName(classBaseName);
        if (before != null && !"".equals(before)) {
            classBaseName = TransferUtil.toClassBaseName(before) + classBaseName;
        }
        if (after != null && !"".equals(after)) {
            classBaseName = TransferUtil.toClassBaseName(after) + classBaseName;
        }
        classBaseName += classType;
        // 保存添加前缀、后缀以及类型后的类名
        classInfo.setClassName(classBaseName);
        // 保存文件名
        classInfo.setFileName(classBaseName + ".java");
        // 保存类的完整路径
        String packagePath = generatorConfig.getPackagePath();
        classInfo.setClassPath(packagePath + "." + classBaseName);

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        int i = 0;
        boolean flag = true;

        // 遍历查询行中是否存在参数
        while ((line = br.readLine()) != null) {
            i++;
            if (i == 1 && !line.startsWith("package ")) {
                continue;
            }
            // 替换行代码中的参数
            line = TransferUtil.replaceParamToValue(classType, line, generatorConfig, classInfo, null);
            code.append(line).append("\n");
            if (line.startsWith("import") && flag) {
                code.append("import " + entityClassPath).append(";").append("\n");
                flag = false;
            }
        }

        classInfo.setCode(code.toString());
        return classInfo;
    }

    public static ClassInfo createServiceImpl(GeneratorConfig generatorConfig, String classType) throws Exception {
        String tableName = generatorConfig.getTableName();
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String entityClassPath = generatorConfig.getEntityClassPath();
        String mapperClassPath = generatorConfig.getMapperClassPath();
        String serviceClassPath = generatorConfig.getServiceClassPath();
        String before = generatorConfig.getClassNameBefore();
        String after = generatorConfig.getClassNameAfter();

        ClassInfo classInfo = ClassInfo.builder().build();
        // 保存根据表名转换的原类名
        classInfo.setOriginClassName(classBaseName);
        if (before != null && !"".equals(before)) {
            classBaseName = TransferUtil.toClassBaseName(before) + classBaseName;
        }
        if (after != null && !"".equals(after)) {
            classBaseName = TransferUtil.toClassBaseName(after) + classBaseName;
        }
        classBaseName += classType;
        // 保存添加前缀、后缀以及类型后的类名
        classInfo.setClassName(classBaseName);
        // 保存文件名
        classInfo.setFileName(classBaseName + ".java");
        // 保存类的完整路径
        String packagePath = generatorConfig.getPackagePath();
        classInfo.setClassPath(packagePath + "." + classBaseName);

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        int i = 0;
        boolean flag = true;

        // 遍历查询行中是否存在参数
        while ((line = br.readLine()) != null) {
            i++;
            if (i == 1 && !line.startsWith("package ")) {
                continue;
            }
            // 替换行代码中的参数
            line = TransferUtil.replaceParamToValue(classType, line, generatorConfig, classInfo, null);
            code.append(line).append("\n");
            if (line.startsWith("import") && flag) {
                code.append("import " + entityClassPath).append(";").append("\n");
                code.append("import " + mapperClassPath).append(";").append("\n");
                code.append("import " + serviceClassPath).append(";").append("\n");
                flag = false;
            }
        }

        classInfo.setCode(code.toString());
        return classInfo;
    }

    public static ClassInfo createMapperXml(GeneratorConfig generatorConfig) throws Exception {
        String mapperClassPath = generatorConfig.getMapperClassPath();
        String mapperClassName = mapperClassPath.substring(mapperClassPath.lastIndexOf(".") + 1);

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        try {
            // 遍历查询行中是否存在参数
            while ((line = br.readLine()) != null) {
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
