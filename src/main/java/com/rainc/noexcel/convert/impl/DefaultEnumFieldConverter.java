package com.rainc.noexcel.convert.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.rainc.noexcel.convert.BaseMapFieldConverter;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 枚举转换器
 *
 * @author rainc
 * @date 2021/8/20
 */
public class DefaultEnumFieldConverter extends BaseMapFieldConverter<Enum<?>> {

    @Override
    @SneakyThrows
    public Map<Enum<?>, String> fieldToExcelDataMap(ExcelFieldMeta excelFieldMeta) {
        Class<Enum<?>> fieldClz = (Class<Enum<?>>) excelFieldMeta.getFieldClz();
        String param = excelFieldMeta.getParam();
        Enum<?>[] enums = fieldClz.getEnumConstants();
        return Arrays.stream(enums).collect(Collectors.toMap(anEnum -> anEnum, anEnum -> {
            if (StrUtil.isEmpty(param)) {
                return anEnum.name();
            } else {
                return ReflectUtil.getFieldValue(anEnum, param).toString();
            }
        },(a,b)->a, LinkedHashMap::new));
    }
}
