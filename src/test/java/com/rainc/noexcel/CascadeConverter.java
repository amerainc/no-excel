package com.rainc.noexcel;

import com.rainc.noexcel.convert.BaseCascadeConverter;
import com.rainc.noexcel.convert.CascadeProvider;
import com.rainc.noexcel.meta.ExcelFieldMeta;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author rainc
 * @date 2021/11/2
 */
public class CascadeConverter extends BaseCascadeConverter<String> implements CascadeProvider {
    @Override
    public Map<String, Map<String, String>> cascadeMap(ExcelFieldMeta excelFieldMeta) {
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        Map<String, String> red = new LinkedHashMap<>();
        red.put("bred","大红色");
        red.put("sred","小红色");
        Map<String, String> yellow = new LinkedHashMap<>();
        yellow.put("byellow","大黄色");
        yellow.put("syellow","小黄色");
        Map<String, String> gold = new LinkedHashMap<>();
        gold.put("bgold","大金黄");
        gold.put("sgold","小金黄");

        map.put("红色",red);
        map.put("黄色", yellow);
        map.put("金黄色",gold);
        return map;
    }
}
