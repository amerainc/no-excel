package com.rainc.noexcel.style;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 * 默认数据样式
 * @author rainc
 * @date 2021/8/15
 */
public class DefaultDataStyleProvider implements StyleProvider{

    @Override
    public void editStyle(CellStyle cellStyle, Workbook workbook) {
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 设置自动换行
        cellStyle.setWrapText(true);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设为文本格式
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));
    }

    @Override
    public void editFont(Font font) {
        font.setBold(Boolean.FALSE);
        font.setFontHeightInPoints((short)10);
    }

}
