package com.rainc.noexcel.convert;

import java.util.List;
import java.util.Map;

/**
 * 级联
 * @author rainc
 * @date 2021/11/2
 */
public interface CascadeProvider {
    /**
     * 级联  Map<级联的excel值,Map<属性值,excel值>>
     * @return
     */
    Map<String, List<String>> cascade();
}
