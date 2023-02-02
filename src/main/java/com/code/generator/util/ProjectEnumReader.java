package com.code.generator.util;


import com.code.generator.enums.ProjectEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Java项目枚举读取器
 *
 * @Date: Created in 2019/10/22 16:58
 */
public class ProjectEnumReader {
    /**
     * 读取ProjectEnum枚举类并返回以升序排序后的集合列表信息
     *
     * @return 以升序排序后的集合列表信息
     */
    public static List<ProjectEnum> getOrderedProjectEnums() {
        ProjectEnum[] projectEnumArray = ProjectEnum.values();

        return Arrays.stream(projectEnumArray).sorted((o1, o2) -> {
            //先按照order排序
            if (!o1.getOrder().equals(o2.getOrder())) {
                return Integer.compare(o1.getOrder(), o2.getOrder());
            }

            //按枚举工程名排序
            return o1.getName().compareTo(o2.getName());
        }).collect(Collectors.toList());
    }
}
