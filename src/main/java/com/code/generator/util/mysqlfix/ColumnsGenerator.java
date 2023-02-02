package com.code.generator.util.mysqlfix;

import com.code.generator.util.mysqldoc.model.Column;
import com.code.generator.util.mysqldoc.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 批量为数据库中的表增加公共字段。
 * 注意：
 * 方法比较暴力，会先删除原来数据库的字段，再增加字段，会丢失原来的数据。
 * 此功能不适合线上数据库操作，仅用于数据库设计阶段，注意！
 *
 * @Date: Created in 2018/12/14
 */
public class ColumnsGenerator {
    /**
     * 数据库连接配置
     */
    private static final String SQL_URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8";
    private static final String SQL_USER = "root";
    private static final String SQL_PASSWORD = "root";

    /**
     * 修改修改的字段列表
     */
    private static final List<String> FIX_COLUMNS = Arrays.asList(new String[]{
            "create_time",
            "update_time",
            "del_flag",
            "data_code",
            "create_by",
            "update_by"});

    /**
     * 增加公共列的方法
     */
    private static final String ALTER_SQL = "ALTER TABLE {table} " +
            "ADD COLUMN create_time BIGINT(20) NOT NULL DEFAULT 0 COMMENT '创建时间',\n" +
            "ADD COLUMN update_time BIGINT(20) NOT NULL DEFAULT 0 COMMENT '更新时间',\n" +
            "ADD COLUMN del_flag TINYINT(3) NOT NULL DEFAULT 0 COMMENT '0-正常,1-已删除',\n" +
            "ADD COLUMN data_code VARCHAR(64) NOT NULL DEFAULT '' COMMENT '数据唯一码',\n" +
            "ADD COLUMN create_by VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建者ID或名称',\n" +
            "ADD COLUMN update_by VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '更新者ID或名称';";

    /**
     * 删除列的方法
     */
    private static final String DROP_SQL = "ALTER TABLE {table} ";

    /**
     * 数据库连接
     */
    private static Connection conn;

    private static Logger log = LoggerFactory.getLogger(ColumnsGenerator.class);

    /**
     * 操作入口
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //获取数据库连接
        conn = getMySQLConnection();

        // 获取数据库下的所有表名称，包含了需要处理的字段列表
        List<Table> tables = getAllTableName();

        // 获得表中所有字段信息
        alterColumns(tables);

        if (conn != null) {
            conn.close();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws Exception
     */
    private static Connection getMySQLConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASSWORD);
        return conn;
    }

    /**
     * 获取数据库的所有表信息
     *
     * @return
     * @throws Exception
     */
    private static List<Table> getAllTableName() throws Exception {
        List<Table> tables = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES");
        while (rs.next()) {
            String tableName = rs.getString(1);
            Table table = new Table(tableName);
            tables.add(table);
        }
        rs.close();
        stmt.close();

        //生成完成的数据表和字段
        buildColumns(tables);

        return tables;
    }

    /**
     * 获取完整的数据库表和字段（字段仅仅是包含的）
     *
     * @param tables
     * @throws Exception
     */
    private static void buildColumns(List<Table> tables) throws Exception {
        Statement stmt = conn.createStatement();
        for (Table table : tables) {
            List<Column> columns = new ArrayList<>();
            ResultSet rs = stmt.executeQuery("show full columns from " + table.getTableName());
            if (rs != null) {
                while (rs.next()) {
                    String field = rs.getString("Field");

                    //包含在公共列中则删除
                    if (FIX_COLUMNS.contains(field)) {
                        Column column = new Column(field, field, "", "", "", "");
                        columns.add(column);
                    }
                }
            }
            if (rs != null) {
                rs.close();
            }
            table.setColumns(columns);
        }
        stmt.close();
    }

    /**
     * 批量移除和添加公共字段
     *
     * @param tables
     * @throws Exception
     */
    private static void alterColumns(List<Table> tables) throws Exception {

        for (Table table : tables) {
            List<Column> columns = table.getColumns();
            if (!CollectionUtils.isEmpty(columns)) {
                StringBuffer dropSql = new StringBuffer(DROP_SQL.replace("{table}", table.getTableName()));
                for (Column column : columns) {

                    if (dropSql.indexOf("DROP") != -1) {
                        dropSql.append(",");
                    }

                    dropSql.append(" DROP COLUMN `" + column.getField() + "`");
                }
                dropSql.append(";");
                log.info(MessageFormat.format("移除表{0}的公共字段：\n{1}", table.getTableName(), dropSql.toString()));
                executeSql(dropSql.toString());
            }
            String alterSql = ALTER_SQL.replace("{table}", table.getTableName());
            log.info(MessageFormat.format("新增表{0}的公共字段：\n{1}", table.getTableName(), alterSql.toString()));
            executeSql(alterSql);
        }

    }

    /**
     * 执行SQL
     *
     * @param sql
     * @throws Exception
     */
    private static void executeSql(String sql) throws Exception {
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        stmt.close();
    }
}
