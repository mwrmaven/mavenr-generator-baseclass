package com.mavenr.generator;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.util.TransferUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author mavenr
 * @Classname ClassGenerator
 * @Description 类生成器
 * @Date 2020/10/16 11:43 下午
 */
public class ClassGenerator {

    /**
     * 创建实体类
     *
     * @param packagePath
     * @param tableName
     * @param columnList
     */
    public ClassInfo createEntity(String packagePath, String tableName, String tableNameCn, List<Column> columnList) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);

        StringBuilder code = new StringBuilder("package " + packagePath + ".entity;\n\n");
        code.append("import io.swagger.annotations.ApiModel;\n" +
                "import io.swagger.annotations.ApiModelProperty;\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import lombok.Builder;\n" +
                "import lombok.Data;\n" +
                "import lombok.NoArgsConstructor;\n\n");
        code.append("@Data\n" +
                "@Builder\n" +
                "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n" +
                "@ApiModel(value = \"").append(tableNameCn).append("实例").append("\")\n");
        code.append("public class " + classBaseName + " {\n\n");

        columnList.forEach(item -> {
            code.append("    @ApiModelProperty(value = \"" + item.getColumnNameCn() + "\")\n");
            code.append("    private " + item.getPropertyType() + " " + item.getPropertyName() + ";\n\n");
        });
        code.append("}");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + ".java")
                .code(code.toString())
                .build();
        return classInfo;
    }


    /**
     * 创建VO类
     *
     * @param packagePath
     * @param tableName
     * @param tableNameCn
     * @param columnList
     */
    public ClassInfo createVO(String packagePath, String tableName, String tableNameCn, List<Column> columnList) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);

        StringBuilder code = new StringBuilder("package " + packagePath + ".vo;\n\n");
        code.append("import io.swagger.annotations.ApiModel;\n" +
                "import io.swagger.annotations.ApiModelProperty;\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import lombok.Data;\n" +
                "import lombok.NoArgsConstructor;\n\n");
        code.append("@Data\n" +
                "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n" +
                "@ApiModel(value = \"").append(tableNameCn).append("VO实例").append("\")\n");
        code.append("public class " + classBaseName + "VO {\n\n");

        columnList.forEach(item -> {
            code.append("    @ApiModelProperty(value = \"" + item.getColumnNameCn() + "\")\n");
            code.append("    private " + item.getPropertyType() + " " + item.getPropertyName() + ";\n\n");
        });
        code.append("    @ApiModelProperty(value = \"当前页\")\n");
        code.append("    private Integer pageNum;\n\n");
        code.append("    @ApiModelProperty(value = \"每页数据条数\")\n");
        code.append("    private Integer pageSize;\n\n");

        code.append("}");
        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "VO.java")
                .code(code.toString())
                .build();
        return classInfo;
    }

    /**
     * 创建Mybatis的xml
     *
     * @param packagePath
     * @param tableName
     * @param columnList
     */
    public ClassInfo createOracleMapperXml(String packagePath, String tableName, List<Column> columnList) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        // 配置xml文件头
        StringBuilder code = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");

        // 配置关联的Mapper java文件
        code.append("<mapper namespace=\"" + packagePath + ".mapper." + classBaseName + "Mapper" + "\">\n\n");

        // 配置基础字段
        code.append("    <sql id=\"All_Columns\">\n");
        code.append("        " + columnList.stream().map(Column::getColumnName).collect(Collectors.joining(", ")) + "\n");
        code.append("    </sql>\n\n");

        // 配置动态插入数据
        code.append("    <!-- 通用——动态插入数据 -->\n");
        code.append("    <insert id=\"insertSelective\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        insert into " + tableName + "\n");
        code.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">\n");
            code.append("                " + item.getColumnName() + ",\n");
            code.append("            </if>\n");
        });
        code.append("        </trim>\n");
        code.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">\n");
            code.append("                #{" + item.getPropertyName() + "},\n");
            code.append("            </if>\n");
        });
        code.append("        </trim>\n");
        code.append("    </insert>\n\n");

        // 配置批量插入数据
        code.append("    <!-- 通用——批量插入数据 -->\n");
        code.append("    <insert id=\"insertList\" parameterType=\"java.util.List\">\n");
        code.append("        insert all \n");
        code.append("        <foreach collection=\"list\" item=\"item\" index=\"index\">\n");
        code.append("            into " + tableName + "(<include refid=\"All_Columns\"/>) values \n");
        code.append("            (\n");
        Stream.iterate(0, i -> i + 1).limit(columnList.size()).forEach(index -> {
            Column column = columnList.get(index);
            if (index == columnList.size() - 1) {
                code.append("                #{item." + column.getPropertyName() + "}\n");
            } else {
                code.append("                #{item." + column.getPropertyName() + "},\n");
            }
        });
        code.append("            )\n");
        code.append("        </foreach>\n");
        code.append("        select 1 from dual\n");
        code.append("    </insert>\n\n");

        // 配置动态查询数据
        code.append("    <!-- 通用——动态查询数据 -->\n");
        code.append("    <select id=\"selectByModel\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\" resultType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        select <include refid=\"All_Columns\"/> from " + tableName + " \n");
        code.append("        <where>\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">and " + item.getColumnName() + " = #{" + item.getPropertyName() + "}</if>\n");
        });
        code.append("        </where>\n");
        code.append("    </select>\n\n");

        // 配置动态更新数据-根据主键更新
        code.append("    <!-- 通用——配置动态更新数据-根据主键更新 -->\n");
        code.append("    <update id=\"updateByPrimaryKeySelective\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        update " + tableName + " \n");
        code.append("        <set>\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">" + item.getColumnName() + " = #{" + item.getPropertyName() + "},</if>\n");
        });
        code.append("        </set>\n");
        Optional<Column> first = columnList.stream().filter(item -> item.isPrimaryKey() == true).findFirst();
        // 判断是否存在主键，如果是，则使用主键，否则使用第一个字段
        if (first.isPresent()) {
            Column column = first.get();
            code.append("        where " + column.getColumnName() + " = #{" + column.getPropertyName() + "}\n");
        } else {
            code.append("        where " + columnList.get(0).getColumnName() + " = #{" + columnList.get(0).getPropertyName() + "}");
        }
        code.append("    </update>");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "Mapper.xml")
                .code(code.toString())
                .build();
        return classInfo;
    }

    /**
     * 创建Mybatis的xml
     *
     * @param packagePath
     * @param tableName
     * @param columnList
     * @return
     */
    public ClassInfo createMysqlMapperXml(String packagePath, String tableName, List<Column> columnList) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        // 配置xml文件头
        StringBuilder code = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");

        // 配置关联的Mapper java文件
        code.append("<mapper namespace=\"" + packagePath + ".mapper." + classBaseName + "Mapper" + "\">\n\n");

        // 配置基础字段
        code.append("    <sql id=\"All_Columns\">\n");
        code.append("        " + columnList.stream().map(Column::getColumnName).collect(Collectors.joining(", ")) + "\n");
        code.append("    </sql>\n\n");

        // 配置动态插入数据
        code.append("    <!-- 通用——动态插入数据 -->\n");
        code.append("    <insert id=\"insertSelective\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        insert into " + tableName + "\n");
        code.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">\n");
            code.append("                " + item.getColumnName() + ",\n");
            code.append("            </if>\n");
        });
        code.append("        </trim>\n");
        code.append("        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">\n");
            code.append("                #{" + item.getPropertyName() + "},\n");
            code.append("            </if>\n");
        });
        code.append("        </trim>\n");
        code.append("    </insert>\n\n");

        // 配置批量插入数据
        code.append("    <!-- 通用——批量插入数据 -->\n");
        code.append("    <insert id=\"insertList\" parameterType=\"java.util.List\">\n");
        code.append("        insert into " + tableName + " values\n");
        code.append("        <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
        code.append("            (\n");
        Stream.iterate(0, i -> i + 1).limit(columnList.size()).forEach(index -> {
            Column column = columnList.get(index);
            if (index == columnList.size() - 1) {
                code.append("                #{item." + column.getPropertyName() + "}\n");
            } else {
                code.append("                #{item." + column.getPropertyName() + "},\n");
            }
        });
        code.append("            )\n");
        code.append("        </foreach>\n");
        code.append("    </insert>\n\n");

        // 配置动态查询数据
        code.append("    <!-- 通用——动态查询数据 -->\n");
        code.append("    <select id=\"selectByModel\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\" resultType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        select <include refid=\"All_Columns\"/> from " + tableName + " \n");
        code.append("        <where>\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">and " + item.getColumnName() + " = #{" + item.getPropertyName() + "}</if>\n");
        });
        code.append("        </where>\n");
        code.append("    </select>\n\n");

        // 配置动态更新数据-根据主键更新
        code.append("    <!-- 通用——配置动态更新数据-根据主键更新 -->\n");
        code.append("    <update id=\"updateByPrimaryKeySelective\" parameterType=\"" + packagePath + ".entity." + classBaseName + "\">\n");
        code.append("        update " + tableName + " \n");
        code.append("        <set>\n");
        columnList.forEach(item -> {
            code.append("            <if test=\"" + item.getPropertyName() + " != null\">" + item.getColumnName() + " = #{" + item.getPropertyName() + "},</if>\n");
        });
        code.append("        </set>\n");
        Optional<Column> first = columnList.stream().filter(item -> item.isPrimaryKey() == true).findFirst();
        // 判断是否存在主键，如果是，则使用主键，否则使用第一个字段
        if (first.isPresent()) {
            Column column = first.get();
            code.append("        where " + column.getColumnName() + " = #{" + column.getPropertyName() + "}\n");
        } else {
            code.append("        where " + columnList.get(0).getColumnName() + " = #{" + columnList.get(0).getPropertyName() + "}");
        }
        code.append("    </update>");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "Mapper.xml")
                .code(code.toString())
                .build();
        return classInfo;
    }

    /**
     * 创建Mapper文件
     *
     * @param packagePath
     * @param tableName
     */
    public ClassInfo createMapper(String packagePath, String tableName) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String variable = TransferUtil.toPropertyName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ".mapper;\n\n");
        code.append("import " + packagePath + ".entity." + classBaseName + ";\n");
        code.append("import org.apache.ibatis.annotations.Mapper;\n\n");
        code.append("import java.util.List;\n\n");
        code.append("@Mapper\n");
        code.append("public interface " + classBaseName + "Mapper {\n\n");
        code.append("    List<" + classBaseName + "> selectByModel(" + classBaseName + " " + variable + ");\n\n");
        code.append("    int insertSelective(" + classBaseName + " " + variable + ");\n\n");
        code.append("    int insertList(List<" + classBaseName + "> insertList);\n\n");
        code.append("    int updateByPrimaryKeySelective(" + classBaseName + " " + variable + ");\n\n");
        code.append("}");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "Mapper.java")
                .code(code.toString())
                .build();
        return classInfo;
    }

    /**
     * 创建Service文件
     *
     * @param packagePath
     * @param tableName
     */
    public ClassInfo createService(String packagePath, String tableName) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String variable = TransferUtil.toPropertyName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ".service;\n\n");
        code.append("import com.github.pagehelper.PageInfo;\n");
        code.append("import " + packagePath + ".entity." + classBaseName + ";\n");
        code.append("import " + packagePath + ".vo." + classBaseName + "VO;\n\n");
        code.append("import java.util.List;\n\n");
        code.append("public interface " + classBaseName + "Service {\n\n");
        code.append("    PageInfo<" + classBaseName + "> selectByModel(" + classBaseName + "VO " + variable + "VO);\n\n");
        code.append("    List<" + classBaseName + "> selectList(" + classBaseName + " " + variable + ");\n\n");
        code.append("    int insertSelective(" + classBaseName + " " + variable + ");\n\n");
        code.append("    int insertList(List<" + classBaseName + "> insertList);\n\n");
        code.append("    int updateByPrimaryKeySelective(" + classBaseName + " " + variable + ");\n\n");
        code.append("}");

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + "Service.java")
                .code(code.toString())
                .build();
        return classInfo;
    }

    /**
     * 创建ServiceImpl文件
     *
     * @param packagePath
     * @param tableName
     * @param columnList
     */
    public ClassInfo createServiceImpl(String packagePath, String tableName, List<Column> columnList) {
        String classBaseName = TransferUtil.toClassBaseName(tableName);
        String variable = TransferUtil.toPropertyName(tableName);
        StringBuilder code = new StringBuilder("package " + packagePath + ".service.impl;\n\n");
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
        code.append("    @Override\n");
        code.append("    public PageInfo<" + classBaseName + "> selectByModel(" + classBaseName + "VO " + variable + "VO) {\n");
        code.append("        " + classBaseName + " " + variable + " = " + classBaseName + ".builder()\n");
        String variableVO = variable + "VO";
        columnList.forEach(item -> {
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
        code.append("    @Override\n");
        code.append("    public List<" + classBaseName + "> selectList(" + classBaseName + " " + variable + ") {\n");
        code.append("        List<" + classBaseName + "> all = " + variable + "Mapper.selectByModel(" + variable + ");\n");
        code.append("        return all;\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public int insertSelective(" + classBaseName + " " + variable + ") {\n");
        code.append("        int result = " + variable + "Mapper.insertSelective(" + variable + ");\n");
        code.append("        return result;\n");
        code.append("    }\n\n");
        code.append("    @Override\n");
        code.append("    public int insertList(List<" + classBaseName + "> insertList" + ") {\n");
        code.append("        int result = " + variable + "Mapper.insertList(insertList);\n");
        code.append("        return result;\n");
        code.append("    }\n\n");
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
