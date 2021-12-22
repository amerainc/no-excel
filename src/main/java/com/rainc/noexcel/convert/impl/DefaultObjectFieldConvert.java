package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.meta.ExcelFieldMeta;

/**
 * 默认objConvert
 * @author rainc
 * @date 2021/8/24
 */
public class DefaultObjectFieldConvert implements FieldConverter<Object> {

    @Override
    public Object parseToField(String excelData) {
        //无法识别的obj就直接返回空
        return null;
    }

    @Override
    public String parseToExcelData(Object fieldData) {
        return fieldData.toString();
    }

    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return true;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
