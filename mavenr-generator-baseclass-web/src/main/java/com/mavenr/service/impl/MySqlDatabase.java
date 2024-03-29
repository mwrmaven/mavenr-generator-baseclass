package com.mavenr.service.impl;

import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.enums.JdbcTypeEnum;
import com.mavenr.util.TransferUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public List<Table> columns(BaseConfig baseConfig) throws Exception {
        // 数据库表名，多个表名使用英文逗号分隔（如果为空，则表示遍历所有表）
        String tableNames = baseConfig.getTableNames();
        String dbName = baseConfig.getName();
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
        for (String item : names) {
            ResultSet resultSet1 = statement.executeQuery("show create table " + item);
            while (resultSet1.next()) {
                List<Column> columns = allColumns(connection, item);
                Table table = Table.builder()
                        .tableName(item)
                        .columns(columns)
                        .owner(dbName)
                        .build();
                String createSql = resultSet1.getString(2);
                int index = createSql.indexOf("COMMENT=");
                if (index != -1) {
                    String tableNameCn = createSql.substring(index + 9).replaceAll("'", "");
                    table.setTableNameCn(tableNameCn);
                }
                tableList.add(table);
            }
        }



        // 关闭连接
        statement.close();
        close();
        return tableList;
    }

    private List<Column> allColumns(Connection connection, String tableName) throws Exception {
        String showColumns = "select * from information_schema.columns where TABLE_NAME = '" + tableName + "'";
        List<Column> columnList = new ArrayList<>();
        Statement statement = connection.createStatement();
        try {
            ResultSet resultSet = statement.executeQuery(showColumns);
            while (resultSet.next()) {
                String columnType = resultSet.getString("DATA_TYPE").toUpperCase();

                ColumnEnum columnEnum = ColumnEnum.getColumnType(columnType);
                String propertyType = columnType;
                if (columnEnum != null) {
                    propertyType = columnEnum.getPropertyType();
                }
                boolean key = false;
                if ("PRI".equals(resultSet.getString("COLUMN_KEY"))) {
                    key = true;
                }
                String columnName = resultSet.getString("COLUMN_NAME");
                String columnNameCn = resultSet.getString("COLUMN_COMMENT");
                if (columnNameCn == null || "".equals(columnNameCn)) {
                    columnNameCn = columnName;
                }
                switch (columnType) {
                    case "INT":
                        columnType = "INTEGER";
                        break;
                    case "DATETIME":
                        columnType = "DATE";
                        break;
                    default:
                }

                // 解析jdbc的type
                String jdbcType = columnType;
                JdbcTypeEnum jdbcTypeEnum = JdbcTypeEnum.getByColumnType(columnType);
                if (jdbcTypeEnum != null) {
                    jdbcType = jdbcTypeEnum.getJdbcType();
                }

                Column column = Column.builder()
                        .columnName(columnName)
                        .columnNameCn(columnNameCn)
                        .columnType(columnType)
                        .index(resultSet.getInt("ORDINAL_POSITION"))
                        .propertyName(TransferUtil.toPropertyName(columnName))
                        .propertyType(propertyType)
                        .primaryKey(key)
                        .jdbcType(jdbcType)
                        .build();
                columnList.add(column);
            }
            statement.close();
        } catch (SQLException e) {
            String msg = "查询表信息的sql语句执行出错！";
            System.out.println(msg + e.getMessage());
            throw e;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    String msg = "数据库连接关闭出错！";
                    System.out.println(msg + e.getMessage());
                    throw e;
                }
            }
        }
        return columnList;
    }
}
