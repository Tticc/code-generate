package com.code.generator.ext;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;

/**
 * @Date: Created in 2018/12/14
 */
public class TyanMybatisPlusTemplateConfig extends TemplateConfig {
    private String reqDto = "/templates/reqDto.java";
    private String rspDto = "/templates/rspDto.java";
    private String bo = "/templates/bo.java";

    public String getReqDTO() {
        return this.reqDto;
    }

    public TemplateConfig setReqDTO(String reqDto) {
        this.reqDto = reqDto;
        return this;
    }

    public String getRspDto() {
        return this.rspDto;
    }

    public TemplateConfig setRspDto(String rspDto) {
        this.reqDto = rspDto;
        return this;
    }

    public String getBO() {
        return this.bo;
    }

    public TemplateConfig setBO(String bo) {
        this.bo = bo;
        return this;
    }
}
