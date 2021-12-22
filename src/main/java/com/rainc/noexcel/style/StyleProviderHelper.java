package com.rainc.noexcel.style;

import cn.hutool.core.util.ReflectUtil;
import com.rainc.noexcel.convert.FieldConverter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 样式提供辅助类
 * @author rainc
 * @date 2021/9/1
 */
public class StyleProviderHelper {
    /**
     * 样式map
     */
    private static final Map<Class<? extends StyleProvider>, StyleProvider> STYLE_PROVIDER_MAP = new ConcurrentHashMap<>(8);


    public static  StyleProvider getStyleProvider(Class<? extends StyleProvider> styleProvider) {
        return Optional.ofNullable(STYLE_PROVIDER_MAP.get(styleProvider))
                .orElseGet(() -> {
                    StyleProvider instance = ReflectUtil.newInstance(styleProvider);
                    STYLE_PROVIDER_MAP.put(styleProvider, instance);
                    return instance;
                }).getInstance();
    }


    public static  void removeFieldConverter(Class<? extends FieldConverter> fieldConverter) {
        STYLE_PROVIDER_MAP.remove(fieldConverter);
    }
}
