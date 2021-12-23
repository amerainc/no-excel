package com.rainc.noexcel;

import com.rainc.noexcel.annotation.ExcelEntity;
import com.rainc.noexcel.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * @author rainc
 * @date 2021/8/12
 */
@ExcelEntity(title = "测试")
@Data
public class TestEntity{
    //require选项必填时输出会带有*号，读取时无必填字段则会抛出异常
    @ExcelField(name = "必填选项", require = true)
    private String str;
    //在枚举类的情况下,param参数可以指定excel写入读取时使用的枚举类属性
    @ExcelField(name = "枚举类转换", param = "i18n")
    private ColorEnum colorEnum;
    @ExcelField(name = "长整型")
    private Long number;
    @ExcelField(name = "时间")
    private Date date;
    @ExcelField(name = "级联",converter = CascadeConverter.class,cascadeDepend = "枚举类转换")
    private String cascade;
}

