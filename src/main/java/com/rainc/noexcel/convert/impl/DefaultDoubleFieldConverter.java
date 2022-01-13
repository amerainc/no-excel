package com.rainc.noexcel.convert.impl;


import com.rainc.noexcel.convert.BaseNumericConverter;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.math.BigDecimal;

/**
 * 默认双精度浮点型转换器
 *
 * @author rainc
 * @date 2021/8/24
 */
public class DefaultDoubleFieldConverter extends BaseNumericConverter<Double> {

    @Override
    protected Double parseToField(BigDecimal bigDecimal) {
       return bigDecimal.doubleValue();
    }

    @Override
    public String parseToExcelData(Double fieldData) {
        return fieldData.toString();
    }

    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return super.match(excelFieldMeta)||double.class==excelFieldMeta.getFieldClz();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
