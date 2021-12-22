package com.rainc.noexcel.convert;

import cn.hutool.core.collection.CollectionUtil;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础级联字段转换器
 * @author rainc
 * @date 2021/11/2
 */
public abstract class BaseCascadeConverter<T> extends BaseMapFieldConverter<T> implements CascadeProvider  {
    /**
     * 级联map
     */
    private Map<String, Map<T,String>> cascadeMap;
    /**
     * 级联map
     * @param excelFieldMeta
     * @return Map<级联的excel值,Map<属性值,excel值>>
     */
    public abstract Map<String, Map<T,String>> cascadeMap(ExcelFieldMeta excelFieldMeta);

    @Override
    public Map<T,String> fieldToExcelDataMap(ExcelFieldMeta excelFieldMeta) {
        this.cascadeMap=cascadeMap(excelFieldMeta);
        Map<T,String> map=new LinkedHashMap<>();
        if (!CollectionUtil.isEmpty(cascadeMap)){
            for (Map<T, String> value : this.cascadeMap.values()) {
                map.putAll(value);
            }
        }
        return map;
    }

    @Override
    public Map<String, List<String>> cascade() {
        Map<String, List<String>> map=new LinkedHashMap<>();
        if (!CollectionUtil.isEmpty(cascadeMap)){
            cascadeMap.forEach((k,v)-> map.put(k,new ArrayList<>(v.values())));
        }
        return map;
    }
}
