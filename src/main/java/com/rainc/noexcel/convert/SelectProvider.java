package com.rainc.noexcel.convert;

import java.util.List;

/**
 * 下拉框提供者
 * @author rainc
 * @date 2021/8/22
 */
public interface SelectProvider {
    /**
     * 下拉框数据
     * @return 下拉框数据
     */
    List<String> select();
}
