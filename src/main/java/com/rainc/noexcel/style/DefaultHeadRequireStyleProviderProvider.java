package com.rainc.noexcel.style;

import org.apache.poi.ss.usermodel.Font;

/**
 * 默认必填标题样式
 * @author rainc
 * @date 2021/8/15
 */
public class DefaultHeadRequireStyleProviderProvider extends DefaultHeadStyleProvider {
    @Override
    public void editFont(Font font) {
        font.setBold(Boolean.FALSE);
        font.setColor(Font.COLOR_RED);
        font.setFontHeightInPoints((short) 10);
    }
}
