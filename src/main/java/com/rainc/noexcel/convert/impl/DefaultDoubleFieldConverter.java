package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

/**
 * 默认双精度浮点型转换器
 *
 * @author rainc
 * @date 2021/8/24
 */
public class DefaultDoubleFieldConverter implements FieldConverter<Double> {
    @Override
    public Double parseToField(String excelData) {
        try {
            return Double.parseDouble(excelData);
        } catch (Exception e) {
          throw new NoExcelException("必须为浮点数");
        }
    }

    @Override
    public String parseToExcelData(Double fieldData) {
        return fieldData.toString();
    }

    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return FieldConverter.super.match(excelFieldMeta)||double.class==excelFieldMeta.getFieldClz();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
