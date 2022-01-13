package com.rainc.noexcel.convert.impl;


import com.rainc.noexcel.convert.BaseNumericConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.math.BigDecimal;

/**
 * 默认整型转换器
 *
 * @author rainc
 * @date 2021/8/22
 */
public class DefaultIntegerFieldConverter extends BaseNumericConverter<Integer> {

    @Override
    protected Integer parseToField(BigDecimal bigDecimal) {
        try {
           return bigDecimal.intValueExact();
        } catch (Exception e) {
            throw new NoExcelException("必须为整型");
        }
    }

    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return super.match(excelFieldMeta)||int.class==excelFieldMeta.getFieldClz();
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
