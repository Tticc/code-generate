package com.code.generator.enums;

import com.code.generator.annotation.TyanEnumValidate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Maven编译构建枚举类
 *
 * @Date: Created in 2019/09/15
 */
@Getter
@AllArgsConstructor
public enum MavenCompileEnum {
    PACKAGE("package", "打包"),
    DEPLOY("deploy", "发布(上传jar至Nexus中间件)");

    @TyanEnumValidate(enumClass = MavenCompileEnum.class, enumMethod = "isValidCode")
    private final String code;
    /**
     * 描述信息
     */
    private final String desc;

    /**
     * 根据枚举状态码获取对应的中文描述信息
     *
     * @param code 枚举状态码
     * @return 描述信息
     */
    public static String getDesc(String code) {
        MavenCompileEnum[] enums = MavenCompileEnum.values();

        for (MavenCompileEnum enumTemp : enums) {
            if (enumTemp.getCode().equals(code)) {
                return enumTemp.getDesc();
            }
        }

        return null;
    }

    /**
     * 根据枚举状态码获取code对应的Enum对象
     *
     * @param code 枚举状态码
     * @return
     */
    public static MavenCompileEnum getEnum(String code) {
        MavenCompileEnum[] enums = MavenCompileEnum.values();

        for (MavenCompileEnum enumTemp : enums) {
            if (enumTemp.getCode().equals(code)) {
                return enumTemp;
            }
        }

        return null;
    }

    /**
     * 判断参数合法性
     */
    public static boolean isValidCode(String code) {
        for (MavenCompileEnum enumTemp : MavenCompileEnum.values()) {
            if (enumTemp.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
