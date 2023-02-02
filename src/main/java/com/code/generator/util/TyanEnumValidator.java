package com.code.generator.util;

import com.code.generator.annotation.TyanEnumValidate;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Date: Created in 2020/03/09 11:30
 */
@Slf4j
public class TyanEnumValidator implements ConstraintValidator<TyanEnumValidate, Object> {
    private Class<? extends Enum<?>> enumClass;
    private String enumMethod;

    @Override
    public void initialize(TyanEnumValidate tyanEnumValidate) {
        enumClass = tyanEnumValidate.enumClass();
        enumMethod = tyanEnumValidate.enumMethod();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return Boolean.TRUE;
        }

        if (enumClass == null || enumMethod == null) {
            return Boolean.TRUE;
        }

        Class<?> valueClass = value.getClass();

        try {
            Method method = enumClass.getMethod(enumMethod, valueClass);
            if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {

                throw new RuntimeException(String.format("%s method return is not boolean type in the %s class", enumMethod, enumClass));
            }

            if (!Modifier.isStatic(method.getModifiers())) {
                throw new RuntimeException(String.format("%s method is not static method in the %s class", enumMethod, enumClass));
            }

            Boolean result = (Boolean) method.invoke(null, value);

            return result == null ? false : result;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(String.format("This %s(%s) method does not exist in the %s", enumMethod, valueClass, enumClass), e);
        }
    }

}
