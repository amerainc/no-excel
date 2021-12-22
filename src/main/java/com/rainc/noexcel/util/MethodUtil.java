package com.rainc.noexcel.util;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import com.rainc.noexcel.meta.BaseErrMsg;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 方法工具
 * @author rainc
 * @date 2021/8/17
 */
public class MethodUtil {
    final public static String GET = "get";
    final public static String IS = "is";
    final public static String SET = "set";


    public static Method getGetMethodWithField(Field field, Class<?> clz) {
        Method method=null;
        try {
            method=clz.getMethod(GET + upperFirst(field.getName()));
        } catch (NoSuchMethodException ignored) {
        }
        return method;
    }


    public static Method getSetMethodWithField(Field field, Class<?> clz) {
        Method method=null;
        try {
            method = clz.getMethod(SET + upperFirst(field.getName()), (Class<?>) field.getGenericType());
        } catch (NoSuchMethodException e) {
            try {
                method=clz.getMethod(IS + upperFirst(field.getName()),(Class<?>) field.getGenericType());
            } catch (NoSuchMethodException ignored) {
            }
        }
        return method;
    }

    public static <T>String getFieldNameWithGetter(Func1<T,?> func1) {
        String methodName = LambdaUtil.getMethodName(func1);
        String fieldName = methodName.replace(GET, "");
        return lowerCaseFirst(fieldName);
    }
    /**
     * 首字母大写
     *
     * @param oldStr
     * @return
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
     * @param oldStr
     * @return
     */
    private static String lowerCaseFirst(String oldStr) {
        char[] chars = oldStr.toCharArray();
        if (chars[0] > 'Z' || chars[0] < 'A') {
            return oldStr;
        }
        chars[0] ^= 32;
        return String.valueOf(chars);
    }

    @SneakyThrows
    public static void main(String[] args) {
//        Class<ExcelFieldMeta> excelFieldMetaClass = ExcelFieldMeta.class;
//        Field field = excelFieldMetaClass.getDeclaredField("require");
//
//        Method setMethodWithField = getSetMethodWithField(field, excelFieldMetaClass);
//        System.out.println(setMethodWithField);
        String fieldName = getFieldNameWithGetter(BaseErrMsg::getErrMsg);
        System.out.println(fieldName);
    }
}
