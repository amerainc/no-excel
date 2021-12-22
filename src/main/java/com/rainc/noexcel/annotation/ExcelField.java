package com.rainc.noexcel.annotation;

import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.convert.impl.DefaultFieldConverter;

import java.lang.annotation.*;

/**
 * excel字段信息
 * @author rainc
 * @date 2021/8/6
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {
    /**
     * 字段名
     */
    String name();

    /**
     * 字段顺序
     */
    int sort() default 0;

    /**
     * 是否必填
     */
    boolean require() default false;

    /**
     * 字段转换器
     */
    Class<? extends FieldConverter<?>> converter() default DefaultFieldConverter.class;

    /**
     * 额外参数
     */
    String param() default "";

    /**
     * 级联依赖字段 字段名(不填默认上一级为前一个字段)
     */
    String cascadeDepend() default "";
}
