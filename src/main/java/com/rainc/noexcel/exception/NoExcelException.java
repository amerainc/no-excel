package com.rainc.noexcel.exception;

/**
 * excel异常基类
 * @author rainc
 * @date 2021/8/11
 */
public class NoExcelException extends RuntimeException{
    public NoExcelException(String message) {
        super(message);
    }
    public NoExcelException(String message,Throwable cause) {
        super(message,cause);
    }
}
