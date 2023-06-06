package com.rainc.noexcel.annotation;

import com.rainc.noexcel.style.*;

import java.lang.annotation.*;

/**
 * excel实体注解
 *
 * @author rainc
 * @date 2021/8/6
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelEntity {
    /**
     * excel导出时的标题名
     */
    String title();

    /**
     * 是否需要错误日志
     * @return
     */
    boolean needErr() default false;
    /**
     * excel是否有标题
     */
    boolean hasTitle() default true;

    /**
     * excel是否有表头
     */
    boolean hasHead() default true;

    /**
     * 读取写入最大数据量
     */
    int maxSize() default 500;

    /**
     * 标题样式
     */
    Class<? extends StyleProvider> titleStyle() default DefaultTitleStyleProvider.class;

    /**
     * 必填内容的表头样式
     */
    Class<? extends StyleProvider> headRequireStyle() default DefaultHeadRequireStyleProviderProvider.class;

    /**
     * 表头样式
     */
    Class<? extends StyleProvider> headStyle() default DefaultHeadStyleProvider.class;

    /**
     * 数据样式
     */
    Class<? extends StyleProvider> dataStyle() default DefaultDataStyleProvider.class;
}
