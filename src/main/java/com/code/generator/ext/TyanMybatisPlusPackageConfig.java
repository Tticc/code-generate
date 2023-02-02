package com.code.generator.ext;

import com.baomidou.mybatisplus.generator.config.PackageConfig;

/**
 * MybatisPlus打包配置类
 *
 * @Date: Created in 2018/12/14
 */
public class TyanMybatisPlusPackageConfig extends PackageConfig {
    /**
     * 请求DTO归属的父级目录
     */
    private String reqDto = "model";
    /**
     * 响应DTO归属的父级目录
     */
    private String rspDto = "model";
    /**
     * 业务BO对象归属的父级目录
     */
    private String bo = "bo";

    public String getReqDTO() {
        return this.reqDto;
    }

    public PackageConfig setReqDto(String reqDto) {
        this.reqDto = reqDto;
        return this;
    }

    public String getRspDto() {
        return this.rspDto;
    }

    public PackageConfig setRspDto(String rspDto) {
        this.rspDto = rspDto;
        return this;
    }

    public String getBO() {
        return this.bo;
    }

    public PackageConfig setBO(String bo) {
        this.bo = bo;
        return this;
    }

}
