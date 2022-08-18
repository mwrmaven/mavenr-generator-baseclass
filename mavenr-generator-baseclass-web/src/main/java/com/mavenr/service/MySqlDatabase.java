package com.mavenr.service;

import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.util.TransferUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author mavenr
 * @Classname MySqlDatabase
 * @Description Mysql数据库的数据表信息获取
 * @Date 2021/12/20 17:19
 */
public class MySqlDatabase extends DatabaseBasic{

    /**
     * 获取表信息
     * @return
     */
    @Override
    public List<Table> columns(BaseConfig baseConfig) throws SQLException {
        // 数据库表名，多个表名使用英文逗号分隔（如果为空，则表示遍历所有表）
        String tableNames = baseConfig.getTableNames();
        // 是否扫描数据库中所有的表
        boolean scanAllTables = null == tableNames || "".equals(tableNames) || "".equals(tableNames.trim());

        Statement statement = connection.createStatement();
        // 表名集合
        List<String> names = new ArrayList<>();
        if (scanAllTables) {
            String showTablesSql = "show tables";
            ResultSet resultSet = statement.executeQuery(showTablesSql);
            while (resultSet.next()) {
                names.add(resultSet.getString(1));
            }
        } else {
            // 获取配置文件中的表名（英文逗号分隔）
            String[] split = tableNames.split(",");
            for (String t : split) {
                names.add(t);
            }
        }

        System.out.println("查询的表名为：" + Arrays.toString(names.toArray()));

        // 遍历表名，获取表信息
        List<Table> tableList = new ArrayList<>();
        names.forEach(item -> {
            try {
                ResultSet resultSet1 = statement.executeQuery("show create table " + item);
                while (resultSet1.next()) {
                    List<Column> columns = allColumns(connection, item);
                    Table table = Table.builder()
                            .tableName(item)
                            .columns(columns)
                            .build();
                    String createSql = resultSet1.getString(2);
                    int index = createSql.indexOf("COMMENT=");
                    if (index != -1) {
                        String tableNameCn = createSql.substring(index + 9).replaceAll("'", "");
                        table.setTableNameCn(tableNameCn);
                    }
                    tableList.add(table);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        // 关闭连接
        statement.close();
        close();
        return tableList;
    }

    private List<Column> allColumns(Connection connection, String tableName) throws SQLException {
        String showColumns = "show full columns from " + tableName;
        List<Column> columnList = new ArrayList<>();
        Statement statement = connection.createStatement();
        try {
            ResultSet resultSet = statement.executeQuery(showColumns);
            while (resultSet.next()) {
                String columnType = resultSet.getString("Type");
                if (columnType.contains("(")) {
                    columnType = columnType.substring(0, columnType.indexOf("("));
                }

                ColumnEnum columnEnum = ColumnEnum.getColumnType(columnType);
                String propertyType = columnType;
                if (columnEnum != null) {
                    propertyType = columnEnum.getPropertyType();
                }
                boolean key = false;
                if ("PRI".equals(resultSet.getString("Key"))) {
                    key = true;
                }
                String columnName = resultSet.getString("Field");
                String columnNameCn = resultSet.getString("Comment");
                if (columnNameCn == null || "".equals(columnNameCn)) {
                    columnNameCn = columnName;
                }
                Column column = Column.builder()
                        .columnName(columnName)
                        .columnNameCn(columnNameCn)
                        .columnType(columnType)
                        .propertyName(TransferUtil.toPropertyName(columnName))
                        .propertyType(propertyType)
                        .primaryKey(key)
                        .build();
                columnList.add(column);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return columnList;
    }
}
