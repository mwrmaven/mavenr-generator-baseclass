package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.TransferUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mavenr
 * @Classname EntityGenerator
 * @Description 实体类生成器
 * @Date 2021/12/22 10:57
 */
public class EntityGenerator implements ClassGeneratorInterface {

    @Override
    public ClassInfo create(String packagePath, String tableName, String tableNameCn, List<Column> columns) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ".entity;\n\n");
        StringBuilder appender = new StringBuilder();


        code.append("import javax.persistence.Column;\n" +
                "import javax.persistence.Table;\n");

        appender.append("@Table(name = \"" + tableName + "\")\n");
        appender.append("public class " + classBaseName + " {\n\n");

        Set<String> classPaths = new HashSet<>();
        columns.forEach(item -> {
            appender.append("    /**\n");
            appender.append("    * " + item.getColumnNameCn() + "\n");
            appender.append("    */\n");
            appender.append("    @Column(name = \"" + item.getColumnName() + "\")\n");
            appender.append("    private " + item.getPropertyType() + " " + item.getPropertyName() + ";\n\n");
            classPaths.add(ColumnEnum.getPropertyType(item.getPropertyType()).getClassPath());
        });
        appender.append("}");

        classPaths.forEach(item -> {
            code.append("import ").append(item).append(";\n");
        });

        code.append("\n").append(appender);
        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + ".java")
                .code(code.toString())
                .build();
        return classInfo;
    }
}
