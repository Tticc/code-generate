package com.code.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 代码生成工具请求参数
 *
 * @Date: Created in 2018/12/14
 */
@ApiModel
@Data
public class CodeGeneratorRQ implements Serializable {
    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "数据库端口")
    private String port;

    @ApiModelProperty(value = "数据库名")
    private String databaseName;

    @ApiModelProperty(value = "登录用户")
    private String userName;

    @ApiModelProperty(value = "登录密码")
    private String passWord;

    @ApiModelProperty(value = "文件存储路径")
    private String storePath;

    @ApiModelProperty(value = "开发者姓名（用于代码注释中使用）")
    private String authorName;

    @ApiModelProperty(value = "包路径")
    private String packages;

    @ApiModelProperty(value = "表名称")
    private List<String> tableNames;

    private String url;

    @ApiModelProperty(value = "设置表前缀")
    private String tablePrefix;

    @ApiModelProperty(value = "表前缀替换内容")
    private String tablePrefixReplace;

    @ApiModelProperty(value = "是否需要替换表前缀")
    private Boolean isReplaceTablePrefix;
}
