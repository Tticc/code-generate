package com.code.generator.ext;

import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Date: Created in 2018/12/14
 */
public class TyanMybatisPlusConfigBuilder extends ConfigBuilder {
    public TyanMybatisPlusConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, TemplateConfig template, GlobalConfig globalConfig) {
        super(packageConfig, dataSourceConfig, strategyConfig, template, globalConfig);

        doAppendPathInfo(packageConfig);
    }

    private void doAppendPathInfo(PackageConfig packageConfig) {
        Map<String, String> pathInfo = getPathInfo();
        TyanMybatisPlusTemplateConfig templateConfig = (TyanMybatisPlusTemplateConfig) getTemplate();
        TyanMybatisPlusPackageConfig selfPackageConfig = (TyanMybatisPlusPackageConfig) packageConfig;

        if (StringUtils.isNotEmpty(templateConfig.getReqDTO())) {
            pathInfo.put("req_path", this.joinPath(getGlobalConfig().getOutputDir(), this.joinPackage(packageConfig.getParent(), selfPackageConfig.getReqDTO())));
        }

        if (StringUtils.isNotEmpty(templateConfig.getRspDto())) {
            pathInfo.put("rsp_path", this.joinPath(getGlobalConfig().getOutputDir(), this.joinPackage(packageConfig.getParent(), selfPackageConfig.getRspDto())));
        }

        /*
        if (StringUtils.isNotEmpty(templateConfig.getBO())) {
            pathInfo.put("bo_path", this.joinPath(getGlobalConfig().getOutputDir(), this.joinPackage(packageConfig.getParent(), selfPackageConfig.getBO())));
        }
        */

        //反射调用父类私有方法设置信息
        Field field = null;
        try {
            Class superClass = this.getClass().getSuperclass();
            field = superClass.getDeclaredField("pathInfo");
            field.setAccessible(true);
            field.set(this, pathInfo);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 利用递归找一个类的指定方法，如果找不到，去父亲里面找直到最上层Object对象为止。
     *
     * @param clazz      目标类
     * @param methodName 方法名
     * @param classes    方法参数类型数组
     * @return 方法对象
     * @throws Exception
     */
    public static Method getMethod(Class clazz, String methodName, final Class[] classes) throws Exception {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, classes);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName,
                            classes);
                }
            }
        }
        return method;
    }

    /**
     * @param obj        调整方法的对象
     * @param methodName 方法名
     * @param classes    参数类型数组
     * @param objects    参数数组
     * @return 方法的返回值
     */
    public static Object invoke(final Object obj, final String methodName, final Class[] classes, final Object[] objects) {
        try {
            Method method = getMethod(obj.getClass(), methodName, classes);
            method.setAccessible(true);// 调用private方法的关键一句话
            return method.invoke(obj, objects);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty("java.io.tmpdir");
        }

        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir = parentDir + File.separator;
        }

        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    private String joinPackage(String parent, String subPackage) {
        return StringUtils.isEmpty(parent) ? subPackage : parent + "." + subPackage;
    }

    private String processName(String name, NamingStrategy strategy) {
        return this.processName(name, strategy, getStrategyConfig().getFieldPrefix());
    }

    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length >= 1) {
            removePrefix = true;
        }

        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            propertyName = name;
        }

        return propertyName;
    }
}
