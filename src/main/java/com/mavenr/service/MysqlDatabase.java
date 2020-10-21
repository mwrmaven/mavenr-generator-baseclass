package com.mavenr.service;

import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.util.TransferUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mavenr
 * @Classname MysqlDatabase
 * @Description Mysql库表信息的解析
 * @Date 2020/10/17 12:17 上午
 */
@Slf4j
public class MysqlDatabase extends DatabaseAbstract {
    @Override
    public List<Column> allColumns(Connection connection, String tableName) throws SQLException {
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
                Column column = Column.builder()
                        .columnName(columnName)
                        .columnNameCn(resultSet.getString("Comment"))
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

    @Override
    public List<Table> allTables(Connection connection) throws SQLException {
        String showTableNames = "show tables";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(showTableNames);
        List<String> tableNames = new ArrayList<>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(1));
        }
        List<Table> tableList = new ArrayList<>();
        tableNames.forEach(item -> {
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

        statement.close();

        return tableList;
    }
}
