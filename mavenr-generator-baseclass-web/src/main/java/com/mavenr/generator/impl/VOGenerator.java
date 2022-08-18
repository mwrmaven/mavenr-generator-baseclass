package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.LombokUtil;
import com.mavenr.util.SwaggerUtil;
import com.mavenr.util.TransferUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mavenr
 * @Classname VOGenerator
 * @Description vo类信息生成器
 * @Date 2021/12/22 11:16
 */
public class VOGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(String packagePath, String tableName, String tableNameCn, List<Column> columns) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        StringBuilder appender = new StringBuilder();
        StringBuilder code = new StringBuilder("package " + packagePath + ".vo;\n\n");

        if (lombok) {
            code.append(LombokUtil.importInfo());
            appender.append(LombokUtil.annotationInfo());
        }

        if (swagger) {
            code.append(SwaggerUtil.importInfo());
            appender.append("@ApiModel(value = \"").append(tableNameCn).append("VO实例").append("\")\n");
        }

        appender.append("public class " + classBaseName + "VO {\n\n");

        Set<String> classPaths = new HashSet<>();
        columns.forEach(item -> {
            if (swagger) {
                appender.append("    @ApiModelProperty(value = \"" + item.getColumnNameCn() + "\")\n");
            }
            appender.append("    private " + item.getPropertyType() + " " + item.getPropertyName() + ";\n\n");
            classPaths.add(ColumnEnum.getPropertyType(item.getPropertyType()).getClassPath());
        });

        classPaths.forEach(item -> {
            code.append("import ").append(item).append(";\n");
        });

        if (swagger) {
            appender.append("    @ApiModelProperty(value = \"当前页\")\n");
        }
        appender.append("    private Integer pageNum;\n\n");
        if (swagger) {
            appender.append("    @ApiModelProperty(value = \"每页数据条数\")\n");
        }
        appender.append("    private Integer pageSize;\n\n");

        appender.append("}");

        code.append("\n").append(appender);
        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "VO.java")
                .code(code.toString())
                .build();
        return classInfo;
    }
}
