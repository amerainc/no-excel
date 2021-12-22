package com.rainc.noexcel.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;


/**
 * 日期格式化工具
 *
 * @author rainc
 * @date 2021/8/24
 */
public class DateFormatUtil {
    /**
     * 解析字符串日期到date
     *
     * @param date 日期时间字符串
     * @return 解析后的Date
     */
    public static Date parseDate(String date) {
        return DateUtil.parse(date);
    }

    /**
     * 通过给定的日期格式匹配并解析日期时间字符串
     *
     * @param date          日期时间字符串
     * @param parsePatterns 需要尝试的日期时间格式数组
     * @return 解析后的Date
     */
    public static Date parseDate(String date, String... parsePatterns) {
        return DateUtil.parse(date, parsePatterns);
    }

    /**
     * 按照指定的格式格式化日期成字符串
     *
     * @param date   日期
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date, String format) {
        return DateUtil.format(date, format);
    }

    /**
     * 解析excel日期格式到java日期
     *
     * @param excelDate excel日期
     * @return 解析后的日期
     */
    public static Date parseExcelDate(double excelDate) {
        return org.apache.poi.ss.usermodel.DateUtil.getJavaDate(excelDate);
    }
}
