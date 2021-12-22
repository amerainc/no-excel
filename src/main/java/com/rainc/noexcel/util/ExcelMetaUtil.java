package com.rainc.noexcel.util;

import cn.hutool.core.util.ReflectUtil;
import com.rainc.noexcel.annotation.ExcelEntity;
import com.rainc.noexcel.annotation.ExcelField;
import com.rainc.noexcel.exception.ExcelEntityNotFoundException;
import com.rainc.noexcel.meta.ExcelEntityMeta;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import com.rainc.noexcel.style.StyleProviderHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * excel元信息工具类
 * @author rainc
 * @date 2021/8/6
 */
public class ExcelMetaUtil {
    /**
     * 获取excel实体信息
     * @param clz 需要获取的类
     * @return 获取到的实体信息
     */
    public static ExcelEntityMeta getExcelEntityMeta(Class<?> clz) {
        return analyseExcelEntityMeta(clz);
    }

    /**
     * 获取excel字段信息
     * @param clz 需要获取的类
     * @return 获取到的字段信息
     */
    public static List<ExcelFieldMeta> getExcelFieldMeta(Class<?> clz) {
        return analyseExcelFieldMeta(clz);
    }

    /**
     * 解析类上excelEntity注解
     *
     * @param clz 需要解析的类
     * @return 解析到的实体信息
     */
    private static ExcelEntityMeta analyseExcelEntityMeta(Class<?> clz) {
        ExcelEntity excelEntity = clz.getAnnotation(ExcelEntity.class);
        if (excelEntity == null) {
            throw new ExcelEntityNotFoundException();
        }
        ExcelEntityMeta excelEntityMeta = AnnotationUtil.annotation2Bean(clz, ExcelEntity.class, ExcelEntityMeta.class);
        excelEntityMeta.setTitleStyle(StyleProviderHelper.getStyleProvider(excelEntity.titleStyle()));
        excelEntityMeta.setDataStyle(StyleProviderHelper.getStyleProvider(excelEntity.dataStyle()));
        excelEntityMeta.setHeadStyle(StyleProviderHelper.getStyleProvider(excelEntity.headStyle()));
        excelEntityMeta.setHeadRequireStyle(StyleProviderHelper.getStyleProvider(excelEntity.headRequireStyle()));
        return excelEntityMeta;
    }

    /**
     * 解析类所有属性上excelField注解
     *
     * @param clz 需要解析的类
     * @return 解析到的注解信息
     */
    public static List<ExcelFieldMeta> analyseExcelFieldMeta(Class<?> clz) {
        Field[] fields = ReflectUtil.getFields(clz);
        return Arrays.stream(fields)
                .filter(field -> field.getAnnotation(ExcelField.class) != null)
                .map(field -> {
                    ExcelFieldMeta excelFieldMeta = AnnotationUtil.annotation2Bean(field, ExcelField.class, ExcelFieldMeta.class);
                    excelFieldMeta.setBelongClz(clz);
                    excelFieldMeta.setFieldName(field.getName());
                    excelFieldMeta.setField(field);
                    excelFieldMeta.setFieldClz(field.getType());
                    excelFieldMeta.setGetMethod(MethodUtil.getGetMethodWithField(field, clz));
                    excelFieldMeta.setSetMethod(MethodUtil.getSetMethodWithField(field, clz));
                    return excelFieldMeta;
                })
                .sorted(Comparator.comparing(ExcelFieldMeta::getSort))
                .collect(Collectors.toList());
    }

}
