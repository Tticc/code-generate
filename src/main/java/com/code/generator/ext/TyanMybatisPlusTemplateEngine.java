package com.code.generator.ext;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Date: Created in 2018/12/14
 */
public class TyanMybatisPlusTemplateEngine extends VelocityTemplateEngine {
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = this.getConfigBuilder().getTableInfoList();
            Iterator i$ = tableInfoList.iterator();

            while (true) {
                TableInfo tableInfo;
                Map objectMap;
                List focList;
                do {
                    do {
                        if (!i$.hasNext()) {
                            return this;
                        }

                        tableInfo = (TableInfo) i$.next();
                        objectMap = this.getObjectMap(tableInfo);
                        Map<String, String> pathInfo = this.getConfigBuilder().getPathInfo();
                        TemplateConfig template = this.getConfigBuilder().getTemplate();

                        TyanMybatisPlusTemplateConfig mybatisPlusTemplateConfig = null;
                        if(template instanceof TyanMybatisPlusTemplateConfig){
                            mybatisPlusTemplateConfig = (TyanMybatisPlusTemplateConfig) template;
                        }

                        String entityName = tableInfo.getEntityName();
                        String controllerFile;
                        if (null != entityName) {
                            controllerFile = String.format((String) pathInfo.get("entity_path") + File.separator + "%s" + this.suffixJavaOrKt(), entityName + "Domain");
                            if (this.isCreate(controllerFile)) {
                                this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getEntity(this.getConfigBuilder().getGlobalConfig().isKotlin())), controllerFile);
                            }
                        }

                        if (null != tableInfo.getMapperName()) {
                            controllerFile = String.format((String) pathInfo.get("mapper_path") + File.separator + tableInfo.getMapperName() + this.suffixJavaOrKt(), entityName);
                            if (this.isCreate(controllerFile)) {
                                this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getMapper()), controllerFile);
                            }
                        }

                        if (null != tableInfo.getXmlName()) {
                            controllerFile = String.format((String) pathInfo.get("xml_path") + File.separator + tableInfo.getXmlName() + ".xml", entityName);
                            if (this.isCreate(controllerFile)) {
                                this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getXml()), controllerFile);
                            }
                        }

                        if (null != tableInfo.getServiceName()) {
                            controllerFile = String.format((String) pathInfo.get("serivce_path") + File.separator + tableInfo.getServiceName() + this.suffixJavaOrKt(), entityName);
                            if (this.isCreate(controllerFile)) {
                                this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getService()), controllerFile);
                            }
                        }

                        if (null != tableInfo.getServiceImplName()) {
                            controllerFile = String.format((String) pathInfo.get("serviceimpl_path") + File.separator + tableInfo.getServiceImplName() + this.suffixJavaOrKt(), entityName);
                            if (this.isCreate(controllerFile)) {
                                this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getServiceImpl()), controllerFile);
                            }
                        }

                        if (null != tableInfo.getControllerName()) {
                            controllerFile = String.format((String) pathInfo.get("controller_path") + File.separator + tableInfo.getControllerName() + this.suffixJavaOrKt(), entityName);
                            if (this.isCreate(controllerFile)) {
                                this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getController()), controllerFile);
                            }
                        }

                        /*** 生成ReqDTO、RspDTO、BO文件 begin ***/
//                        controllerFile = String.format((String) pathInfo.get("req_path") + File.separator + "%s" + this.suffixJavaOrKt(), entityName + "ReqDTO" );
//                        if (this.isCreate(controllerFile)) {
//                            this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getReqDTO()), controllerFile);
//                        }
//                        controllerFile = String.format((String) pathInfo.get("rsp_path") + File.separator + "%s" + this.suffixJavaOrKt(), entityName + "RspDTO" );
//                        if (this.isCreate(controllerFile)) {
//                            this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getRspDto()), controllerFile);
//                        }

                        //RQ、RS(缩写)
                        controllerFile = String.format((String) pathInfo.get("req_path") + File.separator + "%s" + this.suffixJavaOrKt(), entityName + "Request" );
                        if (this.isCreate(controllerFile)) {
                            this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getReqDTO()), controllerFile);
                        }
                        controllerFile = String.format((String) pathInfo.get("rsp_path") + File.separator + "%s" + this.suffixJavaOrKt(), entityName + "Response" );
                        if (this.isCreate(controllerFile)) {
                            this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getRspDto()), controllerFile);
                        }

                        /*
                        //BO
                        controllerFile = String.format((String) pathInfo.get("bo_path") + File.separator + "%s" + this.suffixJavaOrKt(), entityName + "BO" );
                        if (this.isCreate(controllerFile)) {
                            this.writer(objectMap, this.templateFilePath(mybatisPlusTemplateConfig.getBO()), controllerFile);
                        }
                        */
                        /*** 生成ReqDTO、RspDTO、BO文件 end ***/
                    } while (null == this.getConfigBuilder().getInjectionConfig());

                    focList = this.getConfigBuilder().getInjectionConfig().getFileOutConfigList();
                } while (!CollectionUtils.isNotEmpty(focList));

                Iterator itrFocList = focList.iterator();

                while (itrFocList.hasNext()) {
                    FileOutConfig foc = (FileOutConfig) itrFocList.next();
                    if (this.isCreate(foc.outputFile(tableInfo))) {
                        this.writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
            return this;
        }
    }

}
