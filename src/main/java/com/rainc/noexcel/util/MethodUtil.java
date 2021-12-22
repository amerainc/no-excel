package com.rainc.noexcel.util;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import lombok.SneakyThrows;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * 方法工具
 * @author rainc
 * @date 2021/8/17
 */
public class MethodUtil {
    final public static String GET = "get";
    final public static String IS = "is";
    final public static String SET = "set";

    /**
     * 得到属性描述符
     * @param field 属性
     * @param clz 类
     * @return 描述符
     */
    @SneakyThrows
    public static PropertyDescriptor getPropertyDescriptor(Field field, Class<?> clz){
        return new PropertyDescriptor(field.getName(),clz);
    }

    /**
     * 通过get方法拿到属性名
     * @param func1 get方法
     * @return 属性名
     */
    public static <T>String getFieldNameWithGetter(Func1<T,?> func1) {
        String methodName = LambdaUtil.getMethodName(func1);
        String fieldName;
        if (methodName.startsWith(GET)) {
            fieldName = methodName.replaceFirst(GET, "");
        } else if (methodName.startsWith(IS)) {
            fieldName = methodName.replaceFirst(IS, "");
        } else {
            fieldName = methodName;
        }
        return lowerCaseFirst(fieldName);
    }
    /**
     * 首字母大写
     *
     * @param oldStr 需要首字母大写的字符串
     * @return 首字母大写后的字符串
     */
    private static String upperFirst(String oldStr) {
        char[] chars = oldStr.toCharArray();
        if (chars[0] > 'z' || chars[0] < 'a') {
            return oldStr;
        }
        chars[0] ^= 32;
        return String.valueOf(chars);
    }

    /**
     * 首字母小写
     *
     * @param oldStr 需要首字母小写的字符串
     * @return 首字母小写后的字符串
     */
    private static String lowerCaseFirst(String oldStr) {
        char[] chars = oldStr.toCharArray();
        if (chars[0] > 'Z' || chars[0] < 'A') {
            return oldStr;
        }
        chars[0] ^= 32;
        return String.valueOf(chars);
    }
}
