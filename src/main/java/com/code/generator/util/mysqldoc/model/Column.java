package com.code.generator.util.mysqldoc.model;

import lombok.Data;

/**
 * 数据库字段
 */
@Data
public class Column {
    /**
     * 数据库字段名称
     */
    private String field;

    /**
     * 服务端model属性名称
     */
    private String param;

    /**
     * 数据库字段类型
     */
    private String type;

    /**
     * 数据库字段注释
     */
    private String comment;

    /**
     * 是否可以为空
     */
    private String nullable;

    /**
     * 默认值
     */
    private String defaultValue;

    public Column(String field, String param, String type, String comment, String nullable, String defaultValue) {
        this.field = field;
        this.param = param;
        this.type = type;
        this.comment = comment;
        this.nullable = nullable;
        this.defaultValue = defaultValue;
    }
}