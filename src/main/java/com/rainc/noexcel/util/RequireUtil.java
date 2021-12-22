package com.rainc.noexcel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rainc
 * @date 2021/8/24
 */
public class RequireUtil {
    public final static String REQUIRE_TEMPLATE = "*%s";
    public final static String REQUIRE_REGEX = "\\*(.*)";

    public static String titleToRequireTitle(String title) {
        return String.format(REQUIRE_TEMPLATE, title);
    }

    public static String requireTitleToTitle(String requireTitle) {
        Pattern compile = Pattern.compile(REQUIRE_REGEX);
        Matcher matcher = compile.matcher(requireTitle);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return requireTitle;
    }
}
