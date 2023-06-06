package com.rainc.noexcel.meta;

/**
 * @author rainc
 * @date 2023/5/9
 */
public interface ErrMsg {
    /**
     * 有错误信息
     *
     * @return 有错误信息
     */
    boolean hasErrMsg();

    /**
     * 没有错误信息
     *
     * @return 没有错误信息
     */
     boolean hasNotErrMsg();

     void append(String msg);
}
