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
     * 获得第一个泛型的真实类
     * @param clz 需要取得泛型的类
     * @return 泛型真实类
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
    /**
     * 获得第一个泛型的真实类
     * @param genericSuperclass 参数
     * @return 泛型真实类
     */
    private static Class<?> getFirstGenericType(ParameterizedType genericSuperclass) {
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof ParameterizedType) {
           return (Class<?>) ((ParameterizedType) actualTypeArgument).getRawType();
        }
        return (Class<?>) actualTypeArgument;
    }
}
