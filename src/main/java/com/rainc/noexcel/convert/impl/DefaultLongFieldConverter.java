package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

/**
 * 默认长整型转换器
 *
 * @author rainc
 * @date 2021/8/22
 */
public class DefaultLongFieldConverter implements FieldConverter<Long> {

    @Override
    public Long parseToField(String excelData) {
        try {
            return Long.parseLong(excelData);
        } catch (NumberFormatException numberFormatException) {
          throw new NoExcelException("必须为长整型");
        }
    }
    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return FieldConverter.super.match(excelFieldMeta)||long.class==excelFieldMeta.getFieldClz();
    }
    @Override
    public String parseToExcelData(Long fieldData) {
        return fieldData.toString();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
