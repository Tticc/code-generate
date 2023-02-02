package com.code.generator.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 异常枚举读取方法，将源文件中的枚举数据读取出来，便于数据库插入;
 * PS:由于正则比较差劲，只能使用字符截取来做，有能力的同学可重构此方法。
 */
@Slf4j
public class ExceptionEnumReader {
    /**
     * 数据库插入样本，演示的
     */
    private static final String SQL_TMP = "INSERT INTO sys_properties(`ename`,`key`,`error_code`,`cn_msg`, `en_msg`) VALUES ('{0}','{1}','{2}','{3}','error');";

    /**
     * 读取枚举文件，解析成可以入库的数据源
     *
     * @param file
     */
    public static void readFile(File file) {
        try {
            String text = TextFileUtils.readText(file.getCanonicalPath());

            log.debug("fileName:{},fileContent:{}", file.getName(), text);

            //截取枚举的实体类
            String eco = text.substring(text.indexOf("{") + 1, text.indexOf(");"));

            //将文本中的注释，空行全部删除，便于处理
            eco = eco.replaceAll("(/\\*{1,2}[\\s\\S]*?\\*/)|(\\s*|\\t|\\r|\\n)|", "")
                    .replace("\"", "")
                    .replace("(", ",")
                    .replace("),", ";");

            String[] arr = eco.split(";");

            for (String en : arr) {
                String[] lines = en.split(",");
                if (lines.length != 4) {
                    continue;
                }

                log.debug("枚举名称:{},国际化Key:{},错误码:{},中文提示:{}",
                        lines[0],
                        lines[1],
                        lines[2],
                        lines[3]
                );

//                System.out.println("枚举名称：" + lines[0]);
//                System.out.println("国际化Key：" + lines[1]);
//                System.out.println("错误码：" + lines[2]);
//                System.out.println("中文提示：" + lines[3]);

                //组装SQL
                String sql = SQL_TMP;
                for (int i = 0; i < lines.length; i++) {
                    sql = sql.replace("{" + i + "}", lines[i]);
                }

                System.out.println(sql);
                System.out.println("\n");
            }
        } catch (Exception e) {
            log.error("文件:[{}]读取错误,errMsg:{}", file.getName(), e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        //直接读取物理文件
        File file = new File("D:/data/enums");
        File[] files = file.listFiles();
        for (File f : files) {
            //循环读取
            readFile(f);
        }
    }
}
