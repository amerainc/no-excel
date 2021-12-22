package com.rainc.noexcel.meta;

import cn.hutool.core.util.StrUtil;
import com.rainc.noexcel.annotation.ExcelField;
import lombok.Getter;
import lombok.Setter;

/**
 * 错误信息
 * @author zhengyuhen
 * @date 2021/8/23
 */
@Getter
@Setter
public abstract class BaseErrMsg {
    /**
     * 错误信息
     */
    @ExcelField(name = "错误信息",sort = Integer.MAX_VALUE)
    private StringBuffer errMsg;

    /**
     * 有错误信息
     * @return 有错误信息
     */
    public boolean hasErrMsg(){
        return StrUtil.isNotBlank(errMsg);
    }

    /**
     * 没有错误信息
     * @return 没有错误信息
     */
    public boolean hasNotErrMsg(){
        return !hasErrMsg();
    }
}
