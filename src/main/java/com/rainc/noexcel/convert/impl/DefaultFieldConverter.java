package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.convert.FieldConverterHelper;
import com.rainc.noexcel.convert.SelectProvider;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 默认字段转换器
 *
 * @author rainc
 * @date 2021/8/12
 */
public class DefaultFieldConverter implements FieldConverter<Object>, SelectProvider {
    /**
     * 当前代理的字段转换器
     */
    private FieldConverter fieldConverter;

    @Override
    public Object parseToField(String excelData) {
        return fieldConverter.parseToField(excelData);
    }

    @Override
    public String parseToExcelData(Object fieldData) {
        return fieldConverter.parseToExcelData(fieldData);
    }

    @Override
    public void initData(ExcelFieldMeta excelFieldMeta) {
        this.fieldConverter = getFieldConverter(excelFieldMeta);
        //初始化信息
        this.fieldConverter.initData(excelFieldMeta);
    }

    @Override
    public List<String> select() {
        if (this.fieldConverter instanceof SelectProvider) {
            return ((SelectProvider) this.fieldConverter).select();
        }
        return null;
    }

    /**
     * 默认字段转换器匹配列表
     */
    private static final List<FieldConverter> DEFAULT_FIELD_CONVERTER_LIST = new CopyOnWriteArrayList<>();

    static {
        //通过spi加载默认转换器
        ServiceLoader<FieldConverter> serviceLoader = ServiceLoader.load(FieldConverter.class);
        serviceLoader.forEach(DefaultFieldConverter::addFieldConverter);
    }

    /**
     * 添加自定义的字段转换器
     *
     * @param fieldConverters
     */
    public static synchronized void addFieldConverter(FieldConverter<?>... fieldConverters) {
        FieldConverterHelper.addFieldConverter(fieldConverters);
        DEFAULT_FIELD_CONVERTER_LIST.addAll(Arrays.asList(fieldConverters));
        DEFAULT_FIELD_CONVERTER_LIST.sort(Comparator.comparing(FieldConverter::order));
    }

    /**
     * 移除字段转换器
     *
     * @param fieldConverterClz
     * @return
     */
    public static synchronized boolean removeFieldConverterWithClz(Class<? extends FieldConverter<?>> fieldConverterClz) {
        FieldConverterHelper.removeFieldConverter(fieldConverterClz);
        return DEFAULT_FIELD_CONVERTER_LIST.removeIf(fieldConverter -> fieldConverter.getClass() == fieldConverterClz);
    }

    /**
     * 获取字段转换器
     *
     * @param excelFieldMeta
     * @return
     */
    private static FieldConverter<?> getFieldConverter(ExcelFieldMeta excelFieldMeta) {
        for (FieldConverter<?> fieldConverter : DEFAULT_FIELD_CONVERTER_LIST) {
            if (fieldConverter.match(excelFieldMeta)){
                return fieldConverter.getInstance();
            }
        }
        return FieldConverterHelper.getFieldConverter(DefaultObjectFieldConvert.class).getInstance();
    }
}
