package com.rainc.noexcel.convert.impl;

import cn.hutool.core.util.StrUtil;
import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import com.rainc.noexcel.util.DateFormatUtil;
import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Date;

/**
 * 默认时间转换器
 *
 * @author rainc
 * @date 2021/8/24
 */
public class DefaultDateFieldConverter implements FieldConverter<Date> {
    /**
     * 输出时的日期格式
     */
    private String printDateFormat = "yyyy/MM/dd HH:mm:ss";

    @Override
    public Date parseToField(String excelData) {
        //判断是否为excel日期，是就解析excel日期
        try {
            double excelDate = Double.parseDouble(excelData);
            if (DateUtil.isValidExcelDate(excelDate)) {
                return DateFormatUtil.parseExcelDate(excelDate);
            }
        } catch (Exception ignored) {
        }
        //解析普通格式日期
        try {
            return DateFormatUtil.parseDate(excelData);
        } catch (Exception e) {
            throw new NoExcelException("时间格式不正确");
        }
    }

    @Override
    public String parseToExcelData(Date fieldData) {
        return DateFormatUtil.formatDate(fieldData, this.printDateFormat);
    }

    @Override
    public void initData(ExcelFieldMeta excelFieldMeta) {
        //如果有参数则使用参数作为日期格式
        if (StrUtil.isNotBlank(excelFieldMeta.getParam())) {
            this.printDateFormat = excelFieldMeta.getParam();
        }
    }
}
