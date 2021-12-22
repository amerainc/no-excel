package com.rainc.noexcel.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 * 默认头部样式
 * @author rainc
 * @date 2021/8/15
 */
public class DefaultHeadStyleProvider implements StyleProvider{

    @Override
    public void editStyle(CellStyle style, Workbook workbook) {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    @Override
    public void editFont(Font font) {
        font.setBold(Boolean.FALSE);
        font.setFontHeightInPoints((short)10);
    }

}
