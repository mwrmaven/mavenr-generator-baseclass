package com.mavenr.generator.impl;

import com.mavenr.entity.ClassInfo;
import com.mavenr.entity.Column;
import com.mavenr.entity.GeneratorConfig;
import com.mavenr.enums.ClassTypeEnum;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.generator.ClassGeneratorInterface;
import com.mavenr.util.TransferUtil;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mavenr
 * @Classname EntityGenerator
 * @Description 实体类生成器
 * @Date 2021/12/22 10:57
 */
public class EntityGenerator implements ClassGeneratorInterface {



    @Override
    public ClassInfo create(GeneratorConfig generatorConfig) {
        String tableName = generatorConfig.getTableName();
        String classBaseName = TransferUtil.toClassBaseName(tableName);

        // 遍历模板文件的行
        BufferedReader br = generatorConfig.getBr();
        String line = "";
        StringBuilder code = new StringBuilder();
        int i = 0;
        boolean flag = false;
        StringBuilder temp = new StringBuilder();
        try {
            // 遍历查询行中是否存在参数
            while ((line = br.readLine()) != null) {
                i++;
                if (i == 1) {
                    continue;
                }
                if (flag) {
                    temp.append(line).append("\n");
                } else {
                    // 替换行代码中的参数
                    line = TransferUtil.replaceParamToValue(ClassTypeEnum.ENTITY.getClassType(), line, generatorConfig, null);
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
            for(Column item : columnList) {
                // 替换行代码中的参数
                System.out.println("替换的字段信息为：" + item.toString());
                String result = TransferUtil.replaceParamToValue(ClassTypeEnum.ENTITY.getClassType(), columnCode, generatorConfig, item);
                code.append(result);
            }
            code.append("\n}");
        } catch (Exception e) {
            System.out.println("解析模板文件出错！" + e.getMessage());
            e.printStackTrace();
        }

        ClassInfo classInfo = ClassInfo.builder()
                .fileName(classBaseName + ClassTypeEnum.ENTITY.getClassType() + ".java")
                .code(code.toString())
                .build();
        return classInfo;
    }


}
