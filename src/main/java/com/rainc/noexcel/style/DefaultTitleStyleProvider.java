package com.rainc.noexcel.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 * 默认标题样式
 *
 * @author rainc
 * @date 2021/8/15
 */
public class DefaultTitleStyleProvider implements StyleProvider {

    @Override
    public void editStyle(CellStyle cellStyle, Workbook workbook) {
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    @Override
    public void editFont(Font font) {
        font.setColor(HSSFColor.HSSFColorPredefined.VIOLET.getIndex());
        font.setBold(Boolean.FALSE);
        font.setFontHeightInPoints((short) 10);
    }
}
