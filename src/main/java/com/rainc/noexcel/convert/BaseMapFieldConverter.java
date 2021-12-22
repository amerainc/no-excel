package com.rainc.noexcel.convert;

import cn.hutool.core.map.MapUtil;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 基础字段映射转换器
 *
 * @author rainc
 * @date 2021/8/16
 */
public abstract class BaseMapFieldConverter<T> implements FieldConverter<T>, SelectProvider {
    /**
     * 属性和excel值的映射
     */
    private Map<T, String> fieldToExcelDataMap;
    /**
     * excel和属性值的映射
     */
    private Map<String, T> excelDataToFieldMap;

    /**
     * 属性和excel值的映射
     *
     * @param excelFieldMeta 当前字段信息
     * @return 属性和excel值的映射
     */
    public abstract Map<T, String> fieldToExcelDataMap(ExcelFieldMeta excelFieldMeta);

    @Override
    public void initData(ExcelFieldMeta excelFieldMeta) {
        this.fieldToExcelDataMap = this.fieldToExcelDataMap(excelFieldMeta);
        this.excelDataToFieldMap = MapUtil.inverse(this.fieldToExcelDataMap);
    }


    @Override
    public T parseToField(String excelData) {
        T t = excelDataToFieldMap.get(excelData);
        return Optional.ofNullable(t).orElseThrow(()->new NoExcelException("选项不存在"));
    }

    @Override
    public String parseToExcelData(T fieldData) {
        return fieldToExcelDataMap.get(fieldData);
    }

    @Override
    public List<String> select() {
        return new ArrayList<>(fieldToExcelDataMap.values());
    }
}
