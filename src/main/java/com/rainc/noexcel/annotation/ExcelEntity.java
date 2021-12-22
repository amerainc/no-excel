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
     * excel是否显示标题
     */
    boolean showTitle() default true;

    /**
     * excel显示头部
     */
    boolean showHead() default true;

    /**
     * 读取写入最大数据量
     */
    int maxSize() default 500;

    /**
     * 标题样式
     */
    Class<? extends StyleProvider> titleStyle() default DefaultTitleStyleProvider.class;

    /**
     * 必填内容的头部样式
     */
    Class<? extends StyleProvider> headRequireStyle() default DefaultHeadRequireStyleProviderProvider.class;

    /**
     * 头部样式
     */
    Class<? extends StyleProvider> headStyle() default DefaultHeadStyleProvider.class;

    /**
     * 数据样式
     */
    Class<? extends StyleProvider> dataStyle() default DefaultDataStyleProvider.class;
}
