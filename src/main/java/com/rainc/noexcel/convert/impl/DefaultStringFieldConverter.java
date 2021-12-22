package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.FieldConverter;

/**
 * 默认String转换器
 * @author rainc
 * @date 2021/8/22
 */
public class DefaultStringFieldConverter implements FieldConverter<String> {
    @Override
    public String parseToField(String excelData) {
        return excelData;
    }

    @Override
    public String parseToExcelData(String fieldData) {
        return fieldData;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
