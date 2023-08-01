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
            classBaseName = classBaseName + TransferUtil.toClassBaseName(after);
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
            classBaseName = classBaseName + TransferUtil.toClassBaseName(after);
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
            classBaseName = classBaseName + TransferUtil.toClassBaseName(after);
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
                line = TransferUtil.replaceMapperXmlParamToValue(line, generatorConfig);
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

}
