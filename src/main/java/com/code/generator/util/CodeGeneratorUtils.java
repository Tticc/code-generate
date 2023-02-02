package com.code.generator.util;

import com.code.generator.dto.CodeGeneratorRQ;
import com.code.generator.ext.TyanMybatisPlusConfigBuilder;
import com.code.generator.ext.TyanMybatisPlusPackageConfig;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.code.generator.ext.TyanMybatisPlusTemplateConfig;
import com.code.generator.ext.TyanMybatisPlusTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 代码生成器
 *
 * @Date: Created in 2019/09/02 16:58
 */
@Slf4j
public class CodeGeneratorUtils {
    /**
     * DTO对象存储的文件根路径
     */
    private static String DIR_ROOT_DTO = "DTO";
    /**
     * PO对象存储的文件根路径
     */
    private static String DIR_ROOT_PO = "PO";

    /**
     * 代码生成主要执行程序
     *
     * @param codeGeneratorRQ
     * @return
     */
    public static boolean generators(CodeGeneratorRQ codeGeneratorRQ) {
        try {
            String outputDir = codeGeneratorRQ.getStorePath();

            String url = "jdbc:mysql://" + codeGeneratorRQ.getIp() + ":" + codeGeneratorRQ.getPort() + "/"
                    + codeGeneratorRQ.getDatabaseName()
                    + "?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=GMT%2B8";
            codeGeneratorRQ.setUrl(url);

            final String viewOutputDir = outputDir + "/view/";
            AutoGenerator autoGenerator = new AutoGenerator();

            //设置自定义的生成器模板引擎
            autoGenerator.setTemplateEngine(new TyanMybatisPlusTemplateEngine());

            // 全局配置
            GlobalConfig globalConfig = new GlobalConfig();
            globalConfig.setOutputDir(outputDir);
            globalConfig.setFileOverride(true);
            globalConfig.setActiveRecord(true);
            // XML 二级缓存
            globalConfig.setEnableCache(false);
            // XML ResultMap
            globalConfig.setBaseResultMap(true);
            // XML columList
            globalConfig.setBaseColumnList(true);
            globalConfig.setAuthor(codeGeneratorRQ.getAuthorName());
            globalConfig.setServiceName("%sService");
            autoGenerator.setGlobalConfig(globalConfig);

            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setDbType(DbType.MYSQL);
            dsc.setDriverName("com.mysql.jdbc.Driver");
            dsc.setUsername(codeGeneratorRQ.getUserName());
            dsc.setPassword(codeGeneratorRQ.getPassWord());
            dsc.setUrl(codeGeneratorRQ.getUrl());
            autoGenerator.setDataSource(dsc);

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();

            // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
            strategy.setSuperControllerClass("com.lingzhi.retail.dubhe.controller.BaseController");
            // 表名生成策略
            strategy.setNaming(NamingStrategy.underline_to_camel);
            // 设置表前缀
            // 此处可以修改为您的表前缀
            if (null != codeGeneratorRQ.getIsReplaceTablePrefix() && codeGeneratorRQ.getIsReplaceTablePrefix()) {
                strategy.setTablePrefix(codeGeneratorRQ.getTablePrefix(), codeGeneratorRQ.getTablePrefixReplace());
            }
            // 需要生成的表
            List<String> tableNameList = codeGeneratorRQ.getTableNames();
            if (tableNameList != null && tableNameList.size() > 0) {
                String[] tableNames = new String[tableNameList.size()];
                tableNameList.toArray(tableNames);
                strategy.setInclude(tableNames);
            }
            // 排除生成的表
            // strategy.setExclude(new String[]{"test"});
            // 自定义实体父类
            strategy.setSuperEntityClass("com.lingzhi.retail.dubhe.domain.BaseDomain");
            // 自定义实体，公共字段(公共字段将不会生成)
//            String[] commonColumnArr = new String[]{"id", "data_code", "remark", "create_by", "create_time", "update_by", "update_time", "del_flag", "lock_version"};
//            strategy.setSuperEntityColumns(commonColumnArr);
            // 自定义 mapper 父类
            strategy.setSuperMapperClass("BaseMapper");
            // 自定义 service 父类
            strategy.setSuperServiceClass("BaseService");
            // 自定义 service 实现类父类
            strategy.setSuperServiceImplClass("BaseServiceImpl");
            // 自定义 controller 父类
            strategy.setSuperControllerClass("com.lingzhi.retail.middleend.member.controller.BaseController");
            // 【实体】是否生成字段常量（默认 false）
            // public static final String ID = "test_id";
            strategy.setEntityColumnConstant(true);
            // 【实体】是否为构建者模型（默认 false）
            // public User setName(String name) {this.name = name; return this;}
            // strategy.setEntityBuilderModel(true);
            // 【实体】@Data注解（默认 false）
            strategy.setEntityLombokModel(true);

            //不添加@TableField注解
            strategy.entityTableFieldAnnotationEnable(false);

            autoGenerator.setStrategy(strategy);

            /*** 调整为自定义对象，扩展生成原生不支持的文件 begin ***/
//			// 包配置
//			PackageConfig packageConfig = new PackageConfig();
//			packageConfig.setParent(codeGeneratorRQ.getPackages());
//			packageConfig.setController("controller");
//
//			autoGenerator.setPackageInfo(packageConfig);
            /*** 调整为自定义对象，扩展生成原生不支持的文件 end ***/

            // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
            InjectionConfig cfg = new InjectionConfig() {
                @Override
                public void initMap() {
                }
            };
            // 生成的模版路径，不存在时需要先新建
            /*
            File viewDir = new File(viewOutputDir);
            if (!viewDir.exists()) {
                viewDir.mkdirs();
            }
            */

//            List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//            focList.add(new FileOutConfig("/templates/listvue.vue.vm") {
//                @Override
//                public String outputFile(TableInfo tableInfo) {
//                    return getGeneratorViewPath(viewOutputDir, tableInfo, ".vue");
//                }
//            });
//
//            cfg.setFileOutConfigList(focList);

            autoGenerator.setCfg(cfg);

            /*** 设置自定义配置 begin ***/
            TemplateConfig templateConfig = new TyanMybatisPlusTemplateConfig();

            autoGenerator.setTemplate(templateConfig);

            TyanMybatisPlusPackageConfig packageConfig = new TyanMybatisPlusPackageConfig();
            packageConfig.setParent(codeGeneratorRQ.getPackages());
            packageConfig.setController("controller");
            packageConfig.setEntity("domain");
            autoGenerator.setPackageInfo(packageConfig);


            TyanMybatisPlusConfigBuilder configBuilder = new TyanMybatisPlusConfigBuilder(autoGenerator.getPackageInfo(), autoGenerator.getDataSource(), autoGenerator.getStrategy(), autoGenerator.getTemplate(), autoGenerator.getGlobalConfig());
            autoGenerator.setConfig(configBuilder);

            /*** 设置自定义配置 end ***/

            // 生成controller相关
            autoGenerator.execute();

            return true;
        } catch (Exception e) {
            log.error("generators exception,errMsg:" + e.getMessage(), e);
        }

        return false;
    }

    /**
     * 生成请求、响应Dto以及Dao层Po相关对象
     */
    private static boolean generatorDtoAndPo(CodeGeneratorRQ codeGeneratorRQ) {
        String strStorePath = codeGeneratorRQ.getStorePath();
        String strPackages = codeGeneratorRQ.getPackages();

//        // 生成请求、响应Dto以及Dao层Po相关对象
//        generatorDtoAndPo(codeGeneratorRQ);

        return true;
    }

    /**
     * 获取配置文件
     *
     * @return 配置Props
     */
    private static Properties getProperties() {
        // 读取配置文件
        Resource resource = new ClassPathResource("/config/application.properties");
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            log.error("getProperties exception,errMsg:" + e.getMessage(), e);
        }
        return props;
    }

    /**
     * 页面生成的文件名
     */
    private static String getGeneratorViewPath(String viewOutputDir, TableInfo tableInfo, String suffixPath) {
        String name = StringUtils.firstToLowerCase(tableInfo.getEntityName());
        String path = viewOutputDir + "/" + name + "/index" + suffixPath;
        File viewDir = new File(path).getParentFile();
        if (!viewDir.exists()) {
            viewDir.mkdirs();
        }
        return path;
    }
}
