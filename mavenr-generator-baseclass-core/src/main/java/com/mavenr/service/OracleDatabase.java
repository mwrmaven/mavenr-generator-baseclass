package com.mavenr.service;

import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.util.TransferUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mavenr
 * @Classname OracleDatabase
 * @Description Oracle库表信息的解析
 * @Date 2020/10/17 12:15 上午
 */
public class OracleDatabase extends DatabaseAbstract {
    @Override
    public List<Column> allColumns(Connection connection, String tableName) throws SQLException {
        // 获取oracle表的主键
        String showColumns = "select a.COLUMN_NAME, a.DATA_TYPE, u.COMMENTS from user_col_comments u " +
                "left join all_tab_columns a on u.TABLE_NAME = a.TABLE_NAME and a.COLUMN_NAME = u.COLUMN_NAME " +
                "where u.TABLE_NAME = '" + tableName + "'";
//        String showColumns = "select a.COLUMN_NAME, a.DATA_TYPE, u.COMMENTS from " +
//                "(select COLUMN_NAME, COMMENTS from user_col_comments where TABLE_NAME = '" + tableName + "') u " +
//                "left join " +
//                "(select COLUMN_NAME, DATA_TYPE from all_tab_columns where TABLE_NAME = '" + tableName + "') a " +
//                "on a.COLUMN_NAME = u.COLUMN_NAME";
        List<Column> columnList = new ArrayList<>();
        Statement statement = connection.createStatement();
        try {
            ResultSet resultSet = statement.executeQuery(showColumns);
            while (resultSet.next()) {
                String columnType = resultSet.getString("DATA_TYPE");
                if (columnType.contains("(")) {
                    columnType = columnType.substring(0, columnType.indexOf("("));
                }
                ColumnEnum columnEnum = ColumnEnum.getColumnType(columnType);
                String propertyType = columnType;
                if (columnEnum != null) {
                    propertyType = columnEnum.getPropertyType();
                }

                String columnName = resultSet.getString("COLUMN_NAME");
                Column column = Column.builder()
                        .columnName(columnName)
                        .columnNameCn(resultSet.getString("COMMENTS"))
                        .columnType(columnType)
                        .propertyName(TransferUtil.toPropertyName(columnName))
                        .propertyType(propertyType)
                        .build();
                columnList.add(column);
            }

            // 获取表主键
            String showPkOfTable = "select COLUMN_NAME from user_cons_columns cu, user_constraints au " +
                    "where cu.CONSTRAINT_NAME = au.CONSTRAINT_NAME " +
                    "and au.CONSTRAINT_TYPE = 'P' and au.TABLE_NAME = '" + tableName + "'";
            List<String> columnNames = new ArrayList<>();
            ResultSet resultSet1 = statement.executeQuery(showPkOfTable);
            while (resultSet1.next()) {
                columnNames.add(resultSet1.getString("COLUMN_NAME"));
            }
            statement.close();

            // 遍历column，设置key，以及设置字段中文名
            columnList.forEach(item -> {
                if (columnNames.contains(item.getColumnName())) {
                    item.setPrimaryKey(true);
                }
                if (StringUtils.isEmpty(item.getColumnNameCn())) {
                    item.setColumnNameCn(item.getColumnName());
                }
            });
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
        String showTableNames = "select * from USER_TAB_COMMENTS where TABLE_TYPE = 'TABLE'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(showTableNames);
        List<Table> tableList = new ArrayList<>();
        while (resultSet.next()) {
            Table table = Table.builder()
                    .tableName(resultSet.getString("TABLE_NAME"))
                    .tableNameCn(resultSet.getString("COMMENTS"))
                    .build();
            tableList.add(table);
        }

        statement.close();

        for (int i = 0; i < tableList.size(); i++) {
            Table item = tableList.get(i);
            List<Column> columns = allColumns(connection, item.getTableName());
            item.setColumns(columns);
        }

        return tableList;
    }
}
