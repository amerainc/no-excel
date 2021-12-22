package com.rainc.noexcel.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 * 注解工具类
 * @author rainc
 * @date 2021/8/20
 */
public class AnnotationUtil {
    /**
     * 注解转实体类
     * @param annotationEle 标有注解的类
     * @param annotationType 注解类
     * @param clz 转换的实体类
     * @return 转换的实体类
     */
    public static <T> T annotation2Bean(AnnotatedElement annotationEle,Class<? extends Annotation> annotationType, Class<T> clz) {
        T t = ReflectUtil.newInstance(clz);
        Map<String, Object> annotationValueMap = cn.hutool.core.annotation.AnnotationUtil.getAnnotationValueMap(annotationEle, annotationType);
        BeanUtil.fillBeanWithMap(annotationValueMap, t, true);
        return t;
    }
}
