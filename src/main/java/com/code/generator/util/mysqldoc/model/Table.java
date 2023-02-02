package com.code.generator.util.mysqldoc.model;

import lombok.Data;

import java.util.List;

/**
 * 数据表信息
 */
@Data
public class Table {
    /**
     * 数据库表名
     */
    private String tableName;

    /**
     * 数据库表的建表语句
     */
    private String comment;

    /**
     * 表包含的字段
     */
    private List<Column> columns;

    public Table(String tableName) {
        this.tableName = tableName;
    }
}