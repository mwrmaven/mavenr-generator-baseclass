package com.mavenr.service;

import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.util.TransferUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author mavenr
 * @Classname OracleDatabase
 * @Description Oracle数据库的数据表信息获取
 * @Date 2021/12/21 16:45
 */
public class OracleDatabase extends DatabaseBasic{
    @Override
    public List<Table> columns(Properties properties) throws Exception {
        // 数据库表名，多个表名使用英文逗号分隔（若database.scanAllTables=true，则该条不生效）
        String tableNames = properties.getProperty("database.tableNames");
        // 是否扫描数据库中所有的表
        String scanAllTables = properties.getProperty("database.scanAllTables");

        Statement statement = connection.createStatement();
        // 表信息集合
        List<Table> tableList = new ArrayList<>();
        if ("TRUE".equalsIgnoreCase(scanAllTables)) {
            String showTablesSql = "select * from USER_TAB_COMMENTS where TABLE_TYPE = 'TABLE'";
            ResultSet resultSet = statement.executeQuery(showTablesSql);
            while (resultSet.next()) {
                Table table = Table.builder()
                        .tableName(resultSet.getString("TABLE_NAME"))
                        .tableNameCn(resultSet.getString("COMMENTS"))
                        .build();
                tableList.add(table);
            }
        } else {
            // 获取配置文件中的表名（英文逗号分隔）
            String[] split = tableNames.split(",");
            for (String t : split) {
                Table table = Table.builder()
                        .tableName(t)
                        .build();
                tableList.add(table);
            }
        }
        System.out.println("查询的表名为："
                + Arrays.toString(tableList.stream().map(Table::getTableName).collect(Collectors.toList()).toArray()));
        for (Table item : tableList) {
            List<Column> columns = null;
            try {
                columns = allColumns(connection, item.getTableName());
            } catch (SQLException e) {
                String msg = "查询表字段出错！";
                System.out.println(msg + e.getMessage());
                throw new Exception(msg);
            }
            item.setColumns(columns);
        }

        // 关闭连接
        statement.close();
        close();
        return tableList;
    }

    private List<Column> allColumns(Connection connection, String tableName) throws SQLException {
        // 获取oracle表的主键
        String showColumns = "select a.COLUMN_NAME, a.DATA_TYPE, u.COMMENTS, a.DATA_SCALE from user_col_comments u " +
                "left join all_tab_columns a on u.TABLE_NAME = a.TABLE_NAME and a.COLUMN_NAME = u.COLUMN_NAME " +
                "where u.TABLE_NAME = '" + tableName + "'";
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
                if ("NUMBER".equals(columnType)) {
                    // 判断 data_scale的长度，大于0，则为小数，使用BigDecimal接收
                    int dataScale = resultSet.getInt("DATA_SCALE");
                    System.out.println("data_scale length is：" + dataScale);
                    if (dataScale > 0) {
                        columnEnum = ColumnEnum.NUMBERPLUS;
                    }
                }
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
}
