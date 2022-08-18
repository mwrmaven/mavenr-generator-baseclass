package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.AnnotationUtil;
import com.mavenr.util.TransferUtil;

import java.util.List;

/**
 * @author mavenr
 * @Classname ServiceImplGenerator
 * @Description service impl 实现类的生成器
 * @Date 2021/12/22 14:23
 */
public class ServiceImplGenerator implements ClassGeneratorInterface {
    @Override
    public ClassInfo create(String packagePath, String tableName, String tableNameCn, List<Column> columns) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String variable = TransferUtil.toPropertyName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ".service.impl;\n\n");
        code.append("import com.github.pagehelper.Page;\n");
        code.append("import com.github.pagehelper.PageHelper;\n");
        code.append("import com.github.pagehelper.PageInfo;\n");
        code.append("import " + packagePath + ".entity." + classBaseName + ";\n");
        code.append("import " + packagePath + ".mapper." + classBaseName + "Mapper;\n");
        code.append("import " + packagePath + ".vo." + classBaseName + "VO;\n");
        code.append("import " + packagePath + ".service." + classBaseName + "Service;\n");
        code.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        code.append("import org.springframework.stereotype.Service;\n\n");
        code.append("import java.util.List;\n\n");
        code.append("@Service\n");
        code.append("public class " + classBaseName + "ServiceImpl implements " + classBaseName + "Service {\n\n");
        code.append("    @Autowired\n");
        code.append("    private " + classBaseName + "Mapper " + variable + "Mapper;\n\n");
        code.append(AnnotationUtil.functionComment("动态分页查询数据集", variable));
        code.append("    @Override\n");
        code.append("    public PageInfo<" + classBaseName + "> selectByModel(" + classBaseName + "VO " + variable + "VO) {\n");
        code.append("        " + classBaseName + " " + variable + " = " + classBaseName + ".builder()\n");
        String variableVO = variable + "VO";
        columns.forEach(item -> {
            String methodName = item.getPropertyName();
            methodName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            code.append("                ." + item.getPropertyName() + "(" + variableVO + "." + methodName + "())\n");
        });
        code.append("                .build();\n");
        code.append("        Page<Object> objects = PageHelper.startPage(" + variableVO + ".getPageNum(), " + variableVO + ".getPageSize());\n");
        code.append("        List<" + classBaseName + "> all = " + variable + "Mapper.selectByModel(" + variable + ");\n");
        code.append("        PageInfo<" + classBaseName + "> result = PageInfo.of(all);\n");
        code.append("        result.setTotal(objects.getTotal());\n");
        code.append("        result.setPages(objects.getPages());\n");
        code.append("        return result;\n");
        code.append("    }\n\n");
        code.append(AnnotationUtil.functionComment("动态查询数据集", variable));
        code.append("    @Override\n");
        code.append("    public List<" + classBaseName + "> selectList(" + classBaseName + " " + variable + ") {\n");
        code.append("        List<" + classBaseName + "> all = " + variable + "Mapper.selectByModel(" + variable + ");\n");
        code.append("        return all;\n");
        code.append("    }\n\n");
        code.append(AnnotationUtil.functionComment("动态插入数据", variable));
        code.append("    @Override\n");
        code.append("    public int insertSelective(" + classBaseName + " " + variable + ") {\n");
        code.append("        int result = " + variable + "Mapper.insertSelective(" + variable + ");\n");
        code.append("        return result;\n");
        code.append("    }\n\n");
        code.append(AnnotationUtil.functionComment("批量插入数据", "insertList"));
        code.append("    @Override\n");
        code.append("    public int insertList(List<" + classBaseName + "> insertList" + ") {\n");
        code.append("        int result = " + variable + "Mapper.insertList(insertList);\n");
        code.append("        return result;\n");
        code.append("    }\n\n");
        code.append(AnnotationUtil.functionComment("动态更新数据", variable));
        code.append("    @Override\n");
        code.append("    public int updateByPrimaryKeySelective(" + classBaseName + " " + variable + ") {\n");
        code.append("        int result = " + variable + "Mapper.updateByPrimaryKeySelective(" + variable + ");\n");
        code.append("        return result;\n");
        code.append("    }\n\n");

        code.append("}\n");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "ServiceImpl.java")
                .code(code.toString())
                .build();
        return classInfo;
    }
}
