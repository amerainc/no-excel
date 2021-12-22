package com.rainc.noexcel.exception;

/**
 * 找不到注解异常
 * @author rainc
 * @date 2021/8/11
 */
public class ExcelEntityNotFoundException extends NoExcelException{

    public ExcelEntityNotFoundException() {
        super("找不到ExcelEntity注解");
    }
}
