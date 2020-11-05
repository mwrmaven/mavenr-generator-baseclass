package com.mavenr;

import com.mavenr.bo.TableBO;
import com.mavenr.entity.Table;
import com.mavenr.enums.DatabaseTypeEnum;
import com.mavenr.service.DatabaseAbstract;
import com.mavenr.service.DatabaseProxy;
import com.mavenr.service.OutToFile;
import com.mavenr.util.ConnectionUtil;
import com.mavenr.util.WriteOutUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws SQLException {

        System.out.println("默认读取当前路径下的conf.properties配置文件\n" +
                "若要读取指定文件，请使用 -conf 命令参数指定文件的全路径，示例如下：\n" +
                "java -jar base-class-generator.jar -conf ./conf/conf.properties\n\n");

        String defaultFilePath = "." + File.separator + "conf.properties";
        for (int i = 0; i < args.length; i++) {
            if ("-conf".equalsIgnoreCase(args[i])) {
                defaultFilePath = args[++i];
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

        // 数据库类型
        String type = properties.getProperty("database.type");
        if (DatabaseTypeEnum.getType(type) == null) {
            System.out.println("暂不支持该类型数据库！");
            return;
        }
        // 数据库地址
        String address = properties.getProperty("database.address");
        // 数据库端口号
        String port = properties.getProperty("database.port");
        // 数据库名称
        String dbName = properties.getProperty("database.dbName");
        // 数据库用户名
        String username = properties.getProperty("database.username");
        // 数据库密码
        String password = properties.getProperty("database.password");
        // 数据库表名（若database.allTables=true，则该条不生效）
        String tableName = properties.getProperty("database.tableName");

        // 数据库表名注释
        String tableNameCn = properties.getProperty("database.tableNameCn");
        // 是否扫描数据库中所有的表
        String scanAllTables = properties.getProperty("database.scanAllTables");
        // 输出路径
        String outPath = properties.getProperty("database.outPath");
        // 类所在的包基础路径（即entity、vo、server之前的包路径）
        String packagePath = properties.getProperty("database.packagePath");

        if (StringUtils.isEmpty(outPath)) {
            outPath = "." + File.separator;
        } else {
            if (!outPath.endsWith(File.separator)) {
                outPath = outPath  + File.separator;
            }
        }

        // 获取数据库连接
        Connection connection = ConnectionUtil.getConnection(type, address, port, dbName, username, password);
        List<Table> tableList = new ArrayList<>();
        DatabaseAbstract databaseInterface = DatabaseProxy.getDatabaseService(type);
        if ("true".equalsIgnoreCase(scanAllTables)) {
            // 扫描库中所有的表
            tableList = databaseInterface.allTables(connection);
        } else {
            // 扫描指定的表
            tableList = databaseInterface.selectiveTable(connection, tableName, tableNameCn);
        }

        TableBO tableBO = TableBO.builder()
                .packagePath(packagePath)
                .tableList(tableList)
                .build();

        // 根据tableBOList生成code代码并将代码导出到文件
        WriteOutUtil.write(type, tableBO, new OutToFile(), outPath);
        // 关闭数据库连接
        ConnectionUtil.closeConnection(connection);
        System.out.println("程序执行完毕，文件输出路径为：" + outPath);
    }
}
