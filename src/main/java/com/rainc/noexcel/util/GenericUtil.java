package com.rainc.noexcel.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具
 * @author rainc
 * @date 2021/8/24
 */
public class GenericUtil {
    /**
     * 获得第一个泛型类
     * @param clz
     * @return
     */
    public static Class<?> getFirstGenericType(Class<?> clz){
        Type genericSuperclass = clz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType){
            return getFirstGenericType((ParameterizedType) genericSuperclass);
        }
        Type genericInterface = clz.getGenericInterfaces()[0];
        if (genericInterface instanceof ParameterizedType){
            return getFirstGenericType((ParameterizedType) genericInterface);
        }
        return null;
    }

    private static Class<?> getFirstGenericType(ParameterizedType genericSuperclass) {
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof ParameterizedType) {
           return (Class<?>) ((ParameterizedType) actualTypeArgument).getRawType();
        }
        return (Class<?>) actualTypeArgument;
    }

    private static Class<?> getGenClass(Class<?> clz){
        return clz.getSuperclass();
    }

    public static void main(String[] args) {

    }
}
