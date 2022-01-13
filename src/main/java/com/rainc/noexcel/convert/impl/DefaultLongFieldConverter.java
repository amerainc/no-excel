package com.rainc.noexcel.convert.impl;

import com.rainc.noexcel.convert.BaseNumericConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.math.BigDecimal;

/**
 * 默认长整型转换器
 *
 * @author zhengyuchen
 * @date 2021/8/22
 */
public class DefaultLongFieldConverter extends BaseNumericConverter<Long> {

    @Override
    protected Long parseToField(BigDecimal bigDecimal) {
        try {
           return bigDecimal.longValueExact();
        } catch (Exception e) {
            throw new NoExcelException("必须为长整型");
        }
    }
    @Override
    public boolean match(ExcelFieldMeta excelFieldMeta) {
        return super.match(excelFieldMeta)||long.class==excelFieldMeta.getFieldClz();
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
