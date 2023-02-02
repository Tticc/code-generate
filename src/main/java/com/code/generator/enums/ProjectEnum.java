package com.code.generator.enums;

import com.code.generator.annotation.TyanEnumValidate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Java项目枚举
 *
 * @Date: Created in 2019/09/15
 */
@Getter
@AllArgsConstructor
public enum ProjectEnum {
    /*** 基础框架、服务工程 begin ***/
    /*** 基础框架、服务工程 end ***/

    /*** 业务平台、服务工程 begin ***/
    /*** 业务平台、服务工程 end ***/

    ;

    /**
     * 工程名称，比如：tyan-sc-common
     */
    @TyanEnumValidate(enumClass = ProjectEnum.class, enumMethod = "isValidName")
    private final String name;
    /**
     * 顺序(越小优先级越大)，比如：Maven打包构建顺序
     */
    private final Integer order;
    /**
     * Maven编译构建类型
     */
    private final MavenCompileEnum mavenCompileEnum;
    /**
     * jar包是否存在于Target目录
     */
    private final Boolean isJarInTarget;
    /**
     * 是否需要从master拉取feature分支
     * 没有拉取feature分支的项目
     */
    private final Boolean isNeedFeature;
    /**
     * isJarInTarget为false时，需要指定jar包相对于当前工程的相对路径<br/>
     * 如：lib/zipkin-server-2.16.2-exec.jar，全路径为：/opt/tyan-sc-service-zipkin/lib/zipkin-server-2.16.2-exec.jar
     */
    private final String jarFileRelativePath;
    /**
     * 描述信息
     */
    private final String desc;

    /**
     * 根据工程名称获取对应的顺序
     *
     * @param name 工程名称
     * @return 工程顺序
     */
    public static Integer getOrderValue(String name) {
        ProjectEnum[] enums = ProjectEnum.values();

        for (ProjectEnum enumTemp : enums) {
            if (enumTemp.getName().equalsIgnoreCase(name)) {
                return enumTemp.getOrder();
            }
        }

        return null;
    }

    /**
     * 根据工程名称获取对应的Enum对象
     *
     * @param name 工程名称
     * @return
     */
    public static ProjectEnum getEnum(String name) {
        ProjectEnum[] enums = ProjectEnum.values();

        for (ProjectEnum enumTemp : enums) {
            if (enumTemp.getName().equalsIgnoreCase(name)) {
                return enumTemp;
            }
        }

        return null;
    }

    /**
     * 判断参数合法性
     */
    public static boolean isValidName(String name) {
        for (ProjectEnum enumTemp : ProjectEnum.values()) {
            if (enumTemp.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
