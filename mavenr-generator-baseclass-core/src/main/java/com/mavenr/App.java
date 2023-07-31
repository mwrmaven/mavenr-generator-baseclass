package com.mavenr;

import com.mavenr.entity.Table;
import com.mavenr.enums.DatabaseTypeEnum;
import com.mavenr.service.OutToFile;
import com.mavenr.service.DatabaseBasic;
import com.mavenr.service.MySqlDatabase;
import com.mavenr.service.OracleDatabase;
import com.mavenr.util.WriteOutUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        System.out.println("默认读取当前路径下的conf.properties配置文件\n" +
                "若要读取指定文件，请使用 -conf 命令参数指定文件的全路径，示例如下：\n" +
                "java -jar base-class-generator.jar -conf ./conf/conf.properties\n" +
                "如果不需要lombok注解，可以在命令行中添加 -nolombok；\n" +
                "如果不需要swagger注解，可以在命令行中添加 -noswagger；\n\n");

        boolean lombok = true;
        boolean swagger = true;
        String defaultFilePath = "." + File.separator + "conf.properties";
        for (int i = 0; i < args.length; i++) {
            if ("-conf".equalsIgnoreCase(args[i])) {
                defaultFilePath = args[++i];
                continue;
            }
            if ("-nolombok".equals(args[i])) {
                lombok = false;
                continue;
            }
            if ("-noswagger".equals(args[i])) {
                swagger = false;
                continue;
            }
        }
        System.out.println("开始读取配置文件：" + defaultFilePath);

        // 加载properties配置文件
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(new File(defaultFilePath)), "UTF-8"));
        } catch (IOException e) {
            System.out.println(defaultFilePath + " 文件不存在！");
            return;
        }
        properties.setProperty("useLombok", String.valueOf(lombok));
        properties.setProperty("useSwagger", String.valueOf(swagger));

        DatabaseBasic databaseBasic;
        // 数据库驱动
        String driverClassName = properties.getProperty("database.driverClassName");
        if (driverClassName.toUpperCase().contains(DatabaseTypeEnum.ORACLE.getType())) {
            databaseBasic = new OracleDatabase();
        } else if (driverClassName.toUpperCase().contains(DatabaseTypeEnum.MYSQL.getType())) {
            databaseBasic = new MySqlDatabase();
        } else {
            return;
        }

        List<Table> tableList = new ArrayList<>();
        databaseBasic.init(properties);
        try {
            tableList = databaseBasic.columns(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 根据tableBOList生成code代码并将代码导出到文件
        WriteOutUtil.write(tableList, new OutToFile(), properties);
        System.out.println("程序执行完毕，文件输出路径为：" + properties.getProperty("database.outPath"));
    }
}
