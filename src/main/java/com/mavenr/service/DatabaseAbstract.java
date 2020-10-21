package com.mavenr.service;

import com.mavenr.entity.Column;
import com.mavenr.entity.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mavenr
 * @Classname DatabaseAbstract
 * @Description 数据库表、字段接口
 * @Date 2020/10/17 12:13 上午
 */
public abstract class DatabaseAbstract {

    /**
     * 获取表中的字段信息
     *
     * @param connection
     * @param tableName
     * @return
     */
    public abstract List<Column> allColumns(Connection connection, String tableName) throws SQLException;

    /**
     * 获取表信息和表字段信息
     *
     * @param connection
     * @return
     */
    public abstract List<Table> allTables(Connection connection) throws SQLException;

    /**
     * 获取指定表信息和表字段信息
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public List<Table> selectiveTable(Connection connection, String tableName, String tableNameCn) throws SQLException {
        Table table = Table.builder()
                .tableName(tableName)
                .tableNameCn(tableNameCn)
                .columns(allColumns(connection, tableName))
                .build();
        List<Table> result = new ArrayList<>();
        result.add(table);
        return result;
    };
}
