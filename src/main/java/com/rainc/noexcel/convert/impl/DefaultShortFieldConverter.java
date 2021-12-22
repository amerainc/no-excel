package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

/**
 * 默认短整型转换器
 *
 * @author rainc
 * @date 2021/8/22
 */
public class DefaultShortFieldConverter implements FieldConverter<Short> {
    @Override
    public Short parseToField(String excelData) {
        try {
            return Short.parseShort(excelData);
        } catch (NumberFormatException numberFormatException) {
            throw new NoExcelException("必须为短整型");
        }
    }

    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return FieldConverter.super.match(excelFieldMeta) || short.class == excelFieldMeta.getFieldClz();
    }

    @Override
    public String parseToExcelData(Short fieldData) {
        return fieldData.toString();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
