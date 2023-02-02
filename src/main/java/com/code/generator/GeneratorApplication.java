package com.code.generator;

import com.code.generator.dto.CodeGeneratorRQ;
import com.code.generator.util.CodeGeneratorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成工具启动类
 *
 * @Date: Created in 2018/12/14
 */
public class GeneratorApplication {
    public static void main(String[] args) {
        CodeGeneratorRQ codeGeneratorRQ = new CodeGeneratorRQ();
        //包前缀
        codeGeneratorRQ.setPackages("com.test.test");
        //代码签名
        codeGeneratorRQ.setAuthorName("wenc");
        //数据库IP地址
        codeGeneratorRQ.setIp("localhost");
        //数据库IP端口
        codeGeneratorRQ.setPort("3306");
        //数据库名称
        codeGeneratorRQ.setDatabaseName("norgans");
        //生成代码本地目录
        codeGeneratorRQ.setStorePath("C:\\Users\\wenc\\Desktop\\dao");
        //数据库账号
        codeGeneratorRQ.setUserName("root");
        //数据库密码
        codeGeneratorRQ.setPassWord("123456");
        List<String> tableNames = new ArrayList<>();
        tableNames.add("pro_ct");
        tableNames.add("pro_ct_details");


        codeGeneratorRQ.setTableNames(tableNames);

        /* 进行替换表前缀的处理 begin */
//        codeGeneratorRQ.setIsReplaceTablePrefix(true);
//        codeGeneratorRQ.setTablePrefix("");
//        codeGeneratorRQ.setTablePrefixReplace("");
        /* 进行替换表前缀的处理 end */

        //生成代码方法入口
        CodeGeneratorUtils.generators(codeGeneratorRQ);
    }
}
