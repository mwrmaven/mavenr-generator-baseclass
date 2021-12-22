package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.TransferUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mavenr
 * @Classname OracleMapperXmlGenerator
 * @Description oracle mapper xml 文件信息生成器
 * @Date 2021/12/22 11:26
 */
public class OracleMapperXmlGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(String packagePath, String tableName, String tableNameCn, List<Column> columns, boolean lombok, boolean swagger) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        // 配置xml文件头
        StringBuilder code = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");

        // 配置关联的Mapper java文件
        code.append("<mapper namespace=\"" + packagePath + ".mapper." + classBaseName + "Mapper" + "\">\n\n");

        // 配置表名
        code.append("    <sql id=\"TableName\">\n");
        code.append("        " + tableName + "\n");
        code.append("    </sql>\n\n");

        // 配置基础字段
        code.append("    <sql id=\"All_Columns\">\n");
        code.append("        " + columns.stream().map(Column::getColumnName).collect(Collectors.joining(", ")) + "\n");
        code.append("    </sql>\n\n");

        // 配置动态插入数据
        code.append("    <!-- 通用——动态插入数据 -->\n");
        code.append("    <insert id=\"insertSelective\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        insert into " + tableName + "\n");
        code.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        columns.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">\n");
            code.append("                " + item.getColumnName() + ",\n");
            code.append("            </if>\n");
        });
        code.append("        </trim>\n");
        code.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        columns.forEach(item -> {
            String type = item.getColumnType();
            if (type.equals("VARCHAR2")) {
                type = "VARCHAR";
            }
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">\n");
            code.append("                #{" + item.getPropertyName() + ", jdbcType=" + type + "},\n");
            code.append("            </if>\n");
        });
        code.append("        </trim>\n");
        code.append("    </insert>\n\n");

        // 配置批量插入数据
        code.append("    <!-- 通用——批量插入数据 -->\n");
        code.append("    <insert id=\"insertList\" parameterType=\"java.util.List\">\n");
        code.append("        INSERT INTO <include refid=\"TableName\"/> (<include refid=\"All_Columns\">) \n");
        code.append("        SELECT a.* FROM (\n");
        code.append("        <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\"union all\">\n");
        code.append("            SELECT \n");
        Stream.iterate(0, i -> i + 1).limit(columns.size()).forEach(index -> {
            Column column = columns.get(index);
            String type = column.getColumnType();
            if (type.equals("VARCHAR2")) {
                type = "VARCHAR";
            }
            if (index == columns.size() - 1) {
                code.append("                #{item." + column.getPropertyName() + ", jdbcType=" + type + "}\n");
            } else {
                code.append("                #{item." + column.getPropertyName() + ", jdbcType=" + type + "},\n");
            }
        });
        code.append("            FROM dual\n");
        code.append("        </foreach>\n");
        code.append("        ) a\n");
        code.append("    </insert>\n\n");

        // 配置动态查询数据
        code.append("    <!-- 通用——动态查询数据 -->\n");
        code.append("    <select id=\"selectByModel\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\" resultType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        select <include refid=\"All_Columns\"/> from " + tableName + " \n");
        code.append("        <where>\n");
        columns.forEach(item -> {
            String type = item.getColumnType();
            if (type.equals("VARCHAR2")) {
                type = "VARCHAR";
            }
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">and " + item.getColumnName() + " = #{" + item.getPropertyName() + ", jdbcType=" + type + "}</if>\n");
        });
        code.append("        </where>\n");
        code.append("    </select>\n\n");

        // 配置动态更新数据-根据主键更新
        code.append("    <!-- 通用——配置动态更新数据-根据主键更新 -->\n");
        code.append("    <update id=\"updateByPrimaryKeySelective\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        update " + tableName + " \n");
        code.append("        <set>\n");
        columns.forEach(item -> {
            String type = item.getColumnType();
            if (type.equals("VARCHAR2")) {
                type = "VARCHAR";
            }
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">" + item.getColumnName() + " = #{" + item.getPropertyName() + ", jdbcType=" + type + "},</if>\n");
        });
        code.append("        </set>\n");
        Optional<Column> first = columns.stream().filter(item -> item.isPrimaryKey() == true).findFirst();
        // 判断是否存在主键，如果是，则使用主键，否则使用第一个字段
        if (first.isPresent()) {
            Column column = first.get();
            String type = column.getColumnType();
            if (type.equals("VARCHAR2")) {
                type = "VARCHAR";
            }
            code.append("        where " + column.getColumnName() + " = #{" + column.getPropertyName() + ", jdbcType=" + type + "}\n");
        } else {
            Column column = columns.get(0);
            String type = column.getColumnType();
            if (type.equals("VARCHAR2")) {
                type = "VARCHAR";
            }
            code.append("        where " + column.getColumnName() + " = #{" + column.getPropertyName() + ", jdbcType=" + type + "}\n");
        }
        code.append("    </update>\n");
        code.append("</mapper>\n");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "Mapper.xml")
                .code(code.toString())
                .build();
        return classInfo;
    }
}
