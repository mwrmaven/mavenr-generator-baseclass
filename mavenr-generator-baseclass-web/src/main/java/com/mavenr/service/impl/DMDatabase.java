package com.mavenr.service.impl;

import com.mavenr.entity.BaseConfig;
import com.mavenr.entity.Column;
import com.mavenr.entity.Table;
import com.mavenr.enums.ColumnEnum;
import com.mavenr.enums.JdbcTypeEnum;
import com.mavenr.util.TransferUtil;
import com.mysql.cj.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mavenr
 * @Classname DMDatabase
 * @Description TODO
 * @Date 2022/10/25 10:25
 */
public class DMDatabase extends DatabaseBasic{
    @Override
    public List<Table> columns(BaseConfig baseConfig) throws Exception {
        String tableInfo = baseConfig.getTableInfo();
        // 直接处理表信息
        if (!StringUtils.isNullOrEmpty(tableInfo)) {
            List<Table> tables = new ArrayList<>();
            String[] split = tableInfo.split(";");
            int length = split.length;
            // 获取表所属
            String last = split[length - 1];
            String temp = last.substring(20);
            int i = temp.indexOf("\"");
            String owner = temp.substring(0, i);
            // 获取表名
            temp = last.substring(last.indexOf(".") + 2);
            int j = temp.indexOf("\"");
            String tableName = temp.substring(0, j);

            String tableNameCn = "";
            for (int k = 0; k < length; k++) {
                String s = split[k];
                if (s.startsWith("COMMENT ON")) {
                    String t = s.substring(s.indexOf("IS"));
                    t = t.replace("IS", "");
                    t = t.trim();
                    t = t.replaceAll("'", "");
                    tableNameCn = t;
                    break;
                }
            }

            Map<String, Column> columnNameAndType = new HashMap<>();
            Map<String, String> columnComments = new HashMap<>();
            for (int k = 0; k < length; k++) {
                String s = split[k];
                if (k == 0) {
                    // 字段名和类型
                    s = s.substring(s.indexOf("(") + 2);
                    String[] split1 = s.split(",\"");
                    for (int m = 0; m < split1.length; m++) {
                        String s1 = split1[m];
                        String name = s1.substring(0, s1.indexOf("\""));
                        String columnType = "VARCHAR";
                        if (s1.contains("NUMBER")) {
                            // 判断 data_scale的长度，大于0，则为小数，使用BigDecimal接收
                            int dataScale = Integer.parseInt(s1.substring(s1.indexOf(",") + 1, s1.indexOf(")")));
                            System.out.println("data_scale length is：" + dataScale);
                            if (dataScale > 0) {
                                columnType = "DECIMAL";
                            }
                        } else {
                            ColumnEnum[] ces = ColumnEnum.values();
                            for (int n = 0; n < ces.length; n++) {
                                ColumnEnum ce = ces[n];
                                if (s1.contains(ce.getColumnType())) {
                                    columnType = ce.getColumnType();
                                    break;
                                }
                            }
                        }
                        ColumnEnum columnType1 = ColumnEnum.getColumnType(columnType);

                        Column column = Column.builder()
                                .index(m + 1)
                                .columnName(name)
                                .columnType(columnType1.getColumnType())
                                .jdbcType(JdbcTypeEnum.getByColumnType(columnType1.getColumnType()).getJdbcType())
                                .propertyName(TransferUtil.toPropertyName(name))
                                .propertyType(columnType1.getPropertyType())
                                .build();

                        columnNameAndType.put(name, column);
                    }
                } else if (k == 1) {
                    continue;
                } else {
                    // 字段注释
                    s = s.substring(s.indexOf(".") + 1);
                    s = s.substring(s.indexOf(".") + 2);
                    String name = s.substring(0, s.indexOf("\""));
                    s = s.substring(s.indexOf("IS") + 2);
                    s = s.trim();
                    s = s.replaceAll("'", "");
                    String nameCn = s;
                    columnComments.put(name, nameCn);
                }
            }

            List<Column> allColumns = new ArrayList<>();
            for (String cname : columnComments.keySet()) {
                Column column = columnNameAndType.get(cname);
                String comment = columnComments.get(cname);
                column.setColumnNameCn(comment);
                allColumns.add(column);
            }


            Table table = new Table();
            table.setOwner(owner);
            table.setTableName(tableName);
            table.setTableNameCn(tableNameCn);
            table.setColumns(allColumns);

            System.out.println(tableInfo);

            tables.add(table);
            return tables;
        }
        // 数据库表名，多个表名使用英文逗号分隔（如果为空，则表示遍历所有表）
        String tableNames = baseConfig.getTableNames();
        // 是否扫描数据库中所有的表
        boolean scanAllTables = null == tableNames || "".equals(tableNames) || "".equals(tableNames.trim());

        Statement statement = connection.createStatement();
        // 表名集合
        List<Table> allTableList = new ArrayList<>();
        List<Table> tableList = new ArrayList<>();
        String showTablesSql = "select u.*, a.OWNER from USER_TAB_COMMENTS u " +
                "left join ALL_TABLES a on u.TABLE_NAME = a.TABLE_NAME " +
                "where TABLE_TYPE = 'TABLE'";
        ResultSet resultSet = statement.executeQuery(showTablesSql);
        while (resultSet.next()) {
            Table table = Table.builder()
                    .tableName(resultSet.getString("TABLE_NAME"))
                    .tableNameCn(resultSet.getString("COMMENTS"))
                    .owner(resultSet.getString("OWNER"))
                    .build();
            allTableList.add(table);
        }
        Map<String, Table> map = allTableList.stream().collect(Collectors.toMap(Table::getTableName, item -> item, (a, b) -> b));
        if (scanAllTables) {
            tableList = allTableList;
        } else {
            // 获取配置文件中的表名（英文逗号分隔）
            String[] split = tableNames.split(",");
            for (String t : split) {
                Table table = map.get(t.toUpperCase());
                if (table != null) {
                    tableList.add(table);
                }
            }
        }
        System.out.println("查询的表名为：" + Arrays.toString(tableList.stream().map(Table::getTableName).collect(Collectors.toList()).toArray()));

        // 遍历表名，获取表信息
        for (Table item : tableList) {
            List<Column> columns = null;
            columns = allColumns(statement, item.getTableName());
            item.setColumns(columns);
        }
        // 关闭连接
        statement.close();
        close();
        return tableList;
    }

    /**
     * 查询表字段信息
     * @param statement
     * @param tableName
     * @return
     */
    private List<Column> allColumns(Statement statement, String tableName) throws SQLException {
        Map<String, Column> columnMap = new HashMap<>();
        // 查询表字段
        String allColumnsSql = "SELECT * FROM user_tab_columns " +
                "where TABLE_NAME='" + tableName + "'";
        ResultSet resultSet = statement.executeQuery(allColumnsSql);
        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            String columnType = resultSet.getString("DATA_TYPE");
            if ("VARCHAR2".equals(columnType) || columnType.startsWith("NVARCHAR")) {
                columnType = "VARCHAR";
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

            // 解析jdbc的type
            String jdbcType = columnType;
            JdbcTypeEnum jdbcTypeEnum = JdbcTypeEnum.getByColumnType(columnType);
            if (jdbcTypeEnum != null) {
                jdbcType = jdbcTypeEnum.getJdbcType();
            }

            Column column = Column.builder()
                    .columnName(columnName)
                    .columnNameCn(columnName)
                    .columnType(columnType)
                    .index(resultSet.getInt("COLUMN_ID"))
                    .propertyType(propertyType)
                    .propertyName(TransferUtil.toPropertyName(columnName))
                    .jdbcType(jdbcType)
                    .build();
            columnMap.put(columnName, column);
        }

        // 查询字段注释
        String allColumnsCommentSql = "select * from user_col_comments " +
                "where TABLE_NAME='" + tableName + "';";
        ResultSet resultSet1 = statement.executeQuery(allColumnsCommentSql);
        while (resultSet1.next()) {
            String columnName = resultSet1.getString("COLUMN_NAME");
            String comments = resultSet1.getString("COMMENTS");
            if (comments != null && !"".equals(comments.trim())) {
                Column column = columnMap.get(columnName);
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
