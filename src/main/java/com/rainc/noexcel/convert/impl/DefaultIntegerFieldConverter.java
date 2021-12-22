package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

/**
 * 默认整型转换器
 *
 * @author rainc
 * @date 2021/8/22
 */
public class DefaultIntegerFieldConverter implements FieldConverter<Integer> {
    @Override
    public Integer parseToField(String excelData) {
        try {
            return Integer.parseInt(excelData);
        } catch (NumberFormatException numberFormatException) {
            throw new NoExcelException("必须为整型");
        }
    }

    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return FieldConverter.super.match(excelFieldMeta)||int.class==excelFieldMeta.getFieldClz();
    }

    @Override
    public String parseToExcelData(Integer fieldData) {
        return fieldData.toString();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
