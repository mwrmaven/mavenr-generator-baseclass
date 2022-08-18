package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.TransferUtil;

import java.util.List;

/**
 * @author mavenr
 * @Classname ImportBOGenerator
 * @Description 导入模型类信息生成器
 * @Date 2021/12/22 14:25
 */
public class ImportBOGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(String packagePath, String tableName, String tableNameCn, List<Column> columns) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ";\n\n");
        StringBuilder appender = new StringBuilder();

        code.append("import com.clamc.report.submit.business.anno.SubmitReportColumn;\n" +
                "import com.clamc.report.submit.business.anno.SubmitReportTable;\n" +
                "import com.clamc.report.submit.business.constant.SystemParaConstant;\n" +
                "import com.clamc.report.submit.business.sysenum.FieldType;\n" +
                "import com.clamc.report.submit.business.sysenum.FieldValueWay;\n" +
                "import lombok.Data;\n" +
                "import lombok.ToString;\n" +
                "import java.util.Date;\n");

        appender.append("@Data\n" +
                "@ToString\n" +
                "@SubmitReportTable(tableName = \"" + tableName + "\", reportName = \"\", startRow = , endRow = , insertType = \"ALL\")\n");

        appender.append("public class " + classBaseName + "BO {\n\n");

        columns.forEach(item -> {
            String type = item.getColumnType();
            if ("VARCHAR2".equals(type)) {
                type = "VARCHAR";
            }
            appender.append("    @SubmitReportColumn(fieldName = \"" + item.getColumnName() + "\", fieldChineseName = \""
                    + item.getColumnNameCn() + "\", fieldType = FieldType." + type + ", columnIndex = )\n");
            appender.append("    private " + item.getPropertyType() + " " + item.getPropertyName() + ";\n\n");
        });
        appender.append("}");

        code.append("\n").append(appender);
        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "BO.java")
                .code(code.toString())
                .build();
        return classInfo;
    }
}
