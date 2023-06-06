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
public abstract class BaseErrMsg implements ErrMsg{
    /**
     * 错误信息
     */
    @ExcelField(name = "错误信息",sort = Integer.MAX_VALUE)
    private StringBuffer errMsg;

    /**
     * 有错误信息
     * @return 有错误信息
     */
    @Override
    public boolean hasErrMsg(){
        return errMsg!=null;
    }

    /**
     * 没有错误信息
     * @return 没有错误信息
     */
    @Override
    public boolean hasNotErrMsg(){
        return !hasErrMsg();
    }

    @Override
    public void append(String msg) {
        if (hasNotErrMsg()){
            errMsg=new StringBuffer();
        }
        errMsg.append(msg);
    }
}
