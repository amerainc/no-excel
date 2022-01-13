package com.rainc.noexcel.convert;

import com.rainc.noexcel.exception.NoExcelException;

import java.math.BigDecimal;

/**
 * 数字转换器基类
 * @author rainc
 * @date 2022/1/13
 */
public abstract class BaseNumericConverter<T>  implements FieldConverter<T> {
    @Override
    public T parseToField(String excelData) {

        try {
            return parseToField(new BigDecimal(excelData));
        } catch (NoExcelException e) {
            throw e;
        }catch (Exception e){
            throw new NoExcelException("必须是数字类型");
        }
    }

    protected abstract T parseToField(BigDecimal bigDecimal);
}
