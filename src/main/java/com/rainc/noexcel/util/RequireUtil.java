package com.rainc.noexcel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 必填工具类
 * @author rainc
 * @date 2021/8/24
 */
public class RequireUtil {
    public final static String REQUIRE_TEMPLATE = "*%s";
    public final static String REQUIRE_REGEX = "\\*(.*)";

    /**
     * 标题和必填标题的转换
     * @param title 标题
     * @return 必填标题
     */
    public static String titleToRequireTitle(String title) {
        return String.format(REQUIRE_TEMPLATE, title);
    }

    /**
     * 必填标题和标题的转换
     * @param requireTitle 必填标题
     * @return 标题
     */
    public static String requireTitleToTitle(String requireTitle) {
        Pattern compile = Pattern.compile(REQUIRE_REGEX);
        Matcher matcher = compile.matcher(requireTitle);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return requireTitle;
    }
}
