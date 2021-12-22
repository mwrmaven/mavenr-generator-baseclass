package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.AnnotationUtil;
import com.mavenr.util.TransferUtil;

import java.util.List;

/**
 * @author mavenr
 * @Classname MapperGenerator
 * @Description Mapper类信息生成器
 * @Date 2021/12/22 11:29
 */
public class MapperGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(String packagePath, String tableName, String tableNameCn, List<Column> columns, boolean lombok, boolean swagger) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String variable = TransferUtil.toPropertyName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ".mapper;\n\n");
        code.append("import " + packagePath + ".entity." + classBaseName + ";\n");
        code.append("import org.apache.ibatis.annotations.Mapper;\n\n");
        code.append("import java.util.List;\n\n");
        code.append("@Mapper\n");
        code.append("public interface " + classBaseName + "Mapper {\n\n");
        code.append(AnnotationUtil.functionComment("通用——动态查询数据", variable));
        code.append("    List<" + classBaseName + "> selectByModel(" + classBaseName + " " + variable + ");\n\n");
        code.append(AnnotationUtil.functionComment("通用——动态插入数据", variable));
        code.append("    int insertSelective(" + classBaseName + " " + variable + ");\n\n");
        code.append(AnnotationUtil.functionComment("通用——批量插入数据", "insertList"));
        code.append("    int insertList(List<" + classBaseName + "> insertList);\n\n");
        code.append(AnnotationUtil.functionComment("动态更新——根据主键更新", variable));
        code.append("    int updateByPrimaryKeySelective(" + classBaseName + " " + variable + ");\n\n");
        code.append("}");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "Mapper.java")
                .code(code.toString())
                .build();
        return classInfo;
    }
}
