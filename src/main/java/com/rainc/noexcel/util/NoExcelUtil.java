package com.rainc.noexcel.util;

import cn.hutool.core.collection.CollectionUtil;
import com.rainc.noexcel.meta.BaseErrMsg;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具
 *
 * @author rainc
 * @date 2023/3/6
 */
public class NoExcelUtil {
    /**
     * 过滤出错误的数据
     *
     * @param data 数据
     * @param <T>  继承BaseErrMsg
     * @return 解析错误的数据
     */
    public static <T extends BaseErrMsg> List<T> filterErrList(List<T> data) {
        if (CollectionUtil.isEmpty(data)) {
            return null;
        }
        return data.stream().filter(BaseErrMsg::hasErrMsg).collect(Collectors.toList());
    }

    /**
     * 过滤出没问题的数据
     *
     * @param data 数据
     * @param <T>  继承BaseErrMsg
     * @return 没问题的数据
     */
    public static <T extends BaseErrMsg> List<T> filterSucMsg(List<T> data) {
        if (CollectionUtil.isEmpty(data)) {
            return null;
        }
        return data.stream().filter(BaseErrMsg::hasNotErrMsg).collect(Collectors.toList());
    }

    /**
     * 是否有错误的数据
     *
     * @param data 数据
     * @return 有无错误数据
     */
    public static boolean hasErr(List<BaseErrMsg> data) {
        if (CollectionUtil.isEmpty(data)) {
            return false;
        }
        for (BaseErrMsg baseErrMsg : data) {
            if (baseErrMsg.hasErrMsg()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否没有错误的数据
     *
     * @param data 数据
     * @return 有无错误数据
     */
    public static boolean hasNoErr(List<BaseErrMsg> data) {
        return hasErr(data);
    }
}
