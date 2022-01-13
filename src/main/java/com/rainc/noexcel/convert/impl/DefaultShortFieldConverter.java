package com.rainc.noexcel.convert.impl;


import com.rainc.noexcel.convert.BaseNumericConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.math.BigDecimal;

/**
 * 默认短整型转换器
 *
 * @author zhengyuchen
 * @date 2021/8/22
 */
public class DefaultShortFieldConverter extends BaseNumericConverter<Short> {

    @Override
    protected Short parseToField(BigDecimal bigDecimal) {
        try {
            return bigDecimal.shortValueExact();
        } catch (Exception e) {
            throw new NoExcelException("必须为短整型");
        }
    }

    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return super.match(excelFieldMeta) || short.class == excelFieldMeta.getFieldClz();
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
