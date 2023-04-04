package com.rainc.noexcel.convert;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ReflectUtil;
import com.rainc.noexcel.annotation.ExcelField;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字段转换器辅助类
 * @author rainc
 * @date 2021/8/31
 */
public class FieldConverterHelper {
    /**
     * 字段转换器Map
     */
    private static final Map<Class<? extends FieldConverter>, FieldConverter<?>> FIELD_CONVERTER_MAP = new ConcurrentHashMap<>(8);
    /**
     * 字段转换器缓存
     */
    private static final SimpleCache<ExcelFieldMeta, FieldConverter<?>> FIELD_CONVERTER_CACHE = new SimpleCache<>();

    /**
     * 添加字段转换器
     *
     * @param fieldConverters 需要添加的转换器
     */
    public static void addFieldConverter(FieldConverter... fieldConverters) {
        for (FieldConverter<?> fieldConverter : fieldConverters) {
            FIELD_CONVERTER_MAP.put(fieldConverter.getClass(), fieldConverter);
        }
    }

    /**
     * 获取字段转换器实例
     *
     * @param fieldConverter 字段转换器类
     */
    public static FieldConverter<?> getFieldConverter(Class<? extends FieldConverter> fieldConverter) {
        FieldConverter converter = Optional.ofNullable(FIELD_CONVERTER_MAP.get(fieldConverter))
                .orElseGet(() -> {
                    FieldConverter instance = ReflectUtil.newInstance(fieldConverter);
                    FIELD_CONVERTER_MAP.put(fieldConverter, instance);
                    return instance;
                });
        return (FieldConverter) converter.getInstance();
    }

    /**
     * 获取字段转换器实例
     *
     * @param excelFieldMeta 字段信息
     */
    public static FieldConverter getFieldConverter(ExcelFieldMeta excelFieldMeta) {
        return Optional.ofNullable(FIELD_CONVERTER_CACHE.get(excelFieldMeta))
                .orElseGet(() -> {
                    FieldConverter fieldConverter = getFieldConverter((Class<? extends FieldConverter>) excelFieldMeta.getConverter());
                    fieldConverter.initData(excelFieldMeta);
                    FIELD_CONVERTER_CACHE.put(excelFieldMeta,fieldConverter);
                    return fieldConverter;
                });
    }

    /**
     * 移除字段转换器
     *
     * @param fieldConverter 字段转换器类
     */
    public static void removeFieldConverter(Class<? extends FieldConverter> fieldConverter) {
        FIELD_CONVERTER_MAP.remove(fieldConverter);
    }
}
