package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.AnnotationUtil;
import com.mavenr.util.TransferUtil;

import java.util.List;

/**
 * @author mavenr
 * @Classname ServiceGenerator
 * @Description service类信息生成器
 * @Date 2021/12/22 14:22
 */
public class ServiceGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(String packagePath, String tableName, String tableNameCn, List<Column> columns) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String variable = TransferUtil.toPropertyName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ".service;\n\n");
        code.append("import com.github.pagehelper.PageInfo;\n");
        code.append("import " + packagePath + ".entity." + classBaseName + ";\n");
        code.append("import " + packagePath + ".vo." + classBaseName + "VO;\n\n");
        code.append("import java.util.List;\n\n");
        code.append("public interface " + classBaseName + "Service {\n\n");

        code.append(AnnotationUtil.functionComment("动态分页查询数据集", variable));
        code.append("    PageInfo<" + classBaseName + "> selectByModel(" + classBaseName + "VO " + variable + "VO);\n\n");
        code.append(AnnotationUtil.functionComment("动态查询数据集", variable));
        code.append("    List<" + classBaseName + "> selectList(" + classBaseName + " " + variable + ");\n\n");
        code.append(AnnotationUtil.functionComment("动态插入数据", variable));
        code.append("    int insertSelective(" + classBaseName + " " + variable + ");\n\n");
        code.append(AnnotationUtil.functionComment("批量插入数据", "insertList"));
        code.append("    int insertList(List<" + classBaseName + "> insertList);\n\n");
        code.append(AnnotationUtil.functionComment("动态更新数据", variable));
        code.append("    int updateByPrimaryKeySelective(" + classBaseName + " " + variable + ");\n\n");
        code.append("}");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "Service.java")
                .code(code.toString())
                .build();
        return classInfo;
    }
}
