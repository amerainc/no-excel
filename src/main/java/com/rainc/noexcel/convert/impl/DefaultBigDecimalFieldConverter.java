package com.rainc.noexcel.convert.impl;


import com.rainc.noexcel.convert.BaseNumericConverter;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.math.BigDecimal;

/**
 * 默认BigDecimal转换器
 *
 * @author rainc
 * @date 2021/8/24
 */
public class DefaultBigDecimalFieldConverter extends BaseNumericConverter<BigDecimal> {

    @Override
    protected BigDecimal parseToField(BigDecimal bigDecimal) {
        return bigDecimal;
    }

    @Override
    public String parseToExcelData(BigDecimal fieldData) {
        return fieldData.toPlainString();
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
