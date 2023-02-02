package com.code.generator.util.mysqldoc;


import com.code.generator.util.TextFileUtils;
import com.code.generator.util.mysqldoc.model.Column;
import com.code.generator.util.mysqldoc.model.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取MySQL数据表信息，并生产MarkDown格式的文档
 */
public class MyDocGenerator {
    private static final String SQL_URL = "jdbc:mysql://127.0.0.1:3306/test";
    private static final String SQL_USER = "root";
    private static final String SQL_PASSWORD = "root";
    private static final String DOC_OUTPUT = "D:/generateNew/mysqldoc.txt";

    public static void main(String[] args) throws Exception {
        // 获取数据库下的所有表名称
        List<Table> tables = getAllTableName();
        // 获得表的建表语句
        buildTableComment(tables);
        // 获得表中所有字段信息
        buildColumns(tables);
        // 写文件
        write(tables);
    }

    /**
     * 写文件
     */
    private static void write(List<Table> tables) {
        for (Table table : tables) {

            System.out.println(table.getTableName());

//            //先只生成模板和用户模块
//            if (table.getTableName().indexOf("tpl") == -1 && table.getTableName().indexOf("member") == -1
//                    && table.getTableName().indexOf("shop") == -1
//                    && table.getTableName().indexOf("email") == -1
//                    && table.getTableName().indexOf("task") == -1
//                    ) {
//                continue;
//            }

            StringBuilder buffer = new StringBuilder();
            buffer.append("\n------------\n");
            buffer.append("表名：" + table.getTableName() + "<br>");
            buffer.append("说明：" + table.getComment() + "\n\n");
            buffer.append("------------\n");
            buffer.append("|参数|类型|说明|允许空|默认\n");
            buffer.append("|:-------|:-------|:-------|:-------|:-------|\n");
            List<Column> columns = table.getColumns();
            for (Column column : columns) {

                String param = column.getParam();
                if ("del".equals(param) || "delDtm".equals(param)) {
                    continue;
                }
                String type = column.getType();
                String comment = column.getComment();
                buffer.append("|" + param + "|" + type + "|" + ("".equals(comment) ? "无" : comment) + "|" + column.getNullable() + "|" + column.getDefaultValue() + "|\n");
            }
            buffer.append("\n");
            String content = buffer.toString();
            try {
                content = content.replaceAll("'", "\"");
                TextFileUtils.append(DOC_OUTPUT, content);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES");
        while (rs.next()) {
            String tableName = rs.getString(1);
            Table table = new Table(tableName);
            tables.add(table);
        }
        rs.close();
        stmt.close();
        conn.close();
        return tables;
    }

    /**
     * 获得数据表的建表语句
     *
     * @param tables
     * @throws Exception
     */
    private static void buildTableComment(List<Table> tables) throws Exception {
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        for (Table table : tables) {
            ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table.getTableName());
            if (rs != null && rs.next()) {
                String createDDL = rs.getString(2);
                String comment = parse(createDDL);
                table.setComment(comment);
            }
            if (rs != null) {
                rs.close();
            }
        }
        stmt.close();
        conn.close();
    }

    /**
     * 获得数据库的字段信息
     *
     * @param tables
     * @throws Exception
     */
    private static void buildColumns(List<Table> tables) throws Exception {
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        for (Table table : tables) {
            List<Column> columns = new ArrayList<>();
            ResultSet rs = stmt.executeQuery("show full columns from " + table.getTableName());
            if (rs != null) {
                while (rs.next()) {
                    String field = rs.getString("Field");
                    String type = rs.getString("Type");
                    String comment = rs.getString("Comment");
                    String nullable = rs.getString("Null");
                    String defaultValue = rs.getString("Default");
                    if (nullable.equals("YES")) {
                        nullable = "";
                    } else {
                        nullable = "非空";
                    }

                    if (defaultValue == null) {
                        defaultValue = "Null";
                    }

                    if (defaultValue != null && defaultValue.equals("")) {
                        defaultValue = "空字符";
                    }


                    Column column = new Column(field, field, type, comment, nullable, defaultValue);
                    columns.add(column);
                }
            }
            if (rs != null) {
                rs.close();
            }
            table.setColumns(columns);
        }
        stmt.close();
        conn.close();
    }

    /**
     * 提取注释信息
     *
     * @param all
     * @return
     */
    private static String parse(String all) {
        String comment;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }
}
