package com.code.generator.annotation;

import com.code.generator.util.TyanEnumValidator;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 对枚举类型字段进行校验的枚举类
 *
 * @Date: Created in 2019/10/8 14:21
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TyanEnumValidator.class})
@Documented
public @interface TyanEnumValidate {
    /**
     * 提示消息
     *
     * @return
     */
    String message() default StringUtils.EMPTY;

    /**
     * 指定枚举类
     *
     * @return
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 指定枚举类中的校验方法(通过该属性支持多种不同类型的枚举值验证)
     *
     * @return
     */
    String enumMethod();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
