package com.rainc.noexcel;

import com.rainc.noexcel.annotation.ExcelEntity;
import com.rainc.noexcel.annotation.ExcelField;
import com.rainc.noexcel.meta.BaseErrMsg;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author rainc
 * @date 2021/8/12
 */
@ExcelEntity(title = "测试",maxSize = Integer.MAX_VALUE)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class TestEntity extends BaseErrMsg {
    @ExcelField(name = "必填选项", require = true)
    String str;
    @ExcelField(name = "枚举类转换", param = "i18n")
    TrlAlertColorEnum anEnum;
    @ExcelField(name = "长整型")
    short number;
    @ExcelField(name = "时间")
    Date date;
//    @ExcelField(name="级联测试",converter = CascadeConverter.class,cascadeDepend="枚举类转换")
//    String level;
}
