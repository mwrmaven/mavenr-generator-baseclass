package com.mavenr.service;

import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.util.TransferUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author mavenr
 * @Classname DMDatabase
 * @Description TODO
 * @Date 2022/10/25 10:25
 */
public class DMDatabase extends DatabaseBasic{
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
            String showTablesSql = "select * from dba_tab_comments where OWNER= '" + dbName + "'";
            ResultSet resultSet = statement.executeQuery(showTablesSql);
            while (resultSet.next()) {
                names.add(resultSet.getString("TABLE_NAME"));
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
        try {
            for (String item : names) {
                Table table = Table.builder().tableName(item).build();
                // 查询表信息
                String findTableInfoSql = "select * from dba_tab_comments " +
                        "where OWNER= '" + dbName + "' and TABLE_NAME='" + item + "'";
                ResultSet tableInfo = statement.executeQuery(findTableInfoSql);
                while (tableInfo.next()) {
                    String tableNameCn = tableInfo.getString("COMMENTS");
                    table.setTableNameCn(tableNameCn);
                }
                // 查询字段信息
                List<Column> columnsList = allColumns(statement, dbName, item);
                table.setColumns(columnsList);
                tableList.add(table);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            // 关闭连接
            statement.close();
            close();
        }
        return tableList;
    }

    /**
     * 查询表字段信息
     * @param statement
     * @param dbName
     * @param tableName
     * @return
     */
    private List<Column> allColumns(Statement statement, String dbName, String tableName) throws SQLException {
        Map<String, Column> columnMap = new HashMap<>();
        // 查询表字段
        String allColumnsSql = "select * from dba_tab_columns " +
                "where OWNER='" + dbName + "' and TABLE_NAME='" + tableName + "';";
        ResultSet resultSet = statement.executeQuery(allColumnsSql);
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnType = resultSet.getString("DATA_TYPE");
            ColumnEnum columnEnum = ColumnEnum.getColumnType(columnType);
            String propertyType = columnType;
            if (columnEnum != null) {
                propertyType = columnEnum.getPropertyType();
            }
            Column column = Column.builder()
                    .columnName(columnName)
                    .columnType(columnType)
                    .propertyType(propertyType)
                    .propertyName(TransferUtil.toPropertyName(columnName))
                    .build();
            columnMap.put(columnName, column);
        }

        // 查询字段注释
        String allColumnsCommentSql = "select * from user_col_comments " +
                "where OWNER='" + dbName + "' and TABLE_NAME='" + tableName + "';";
        ResultSet resultSet1 = statement.executeQuery(allColumnsCommentSql);
        while (resultSet1.next()) {
            String columnName = resultSet1.getString("COLUMN_NAME");
            String comments = resultSet1.getString("COMMENTS");
            if (comments == null || "".equals(comments)) {
                comments = columnName;
            }
            Column column = columnMap.get(columnName);
            if (column == null) {
                column = Column.builder().columnName(columnName).columnNameCn(comments).build();
                columnMap.put(columnName, column);
            } else {
                column.setColumnNameCn(comments);
            }
        }

        List<Column> result = new ArrayList<>();
        columnMap.forEach((k, v) -> {
            result.add(v);
        });
        return result;
    }


}
