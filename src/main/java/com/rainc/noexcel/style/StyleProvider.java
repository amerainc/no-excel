package com.rainc.noexcel.style;

import com.rainc.noexcel.InstanceProvider;
import com.rainc.noexcel.write.ExcelWriter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 样式提供者
 *
 * @author rainc
 * @date 2021/8/15
 */
public interface StyleProvider extends InstanceProvider<StyleProvider> {
    /**
     * 获取样式
     * @param excelWriter excelWriter实例
     * @return 样式
     */
    default CellStyle getStyle(ExcelWriter<?> excelWriter) {
        Workbook workbook = excelWriter.getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        editStyle(cellStyle,workbook);
        Font font = workbook.createFont();
        editFont(font);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 编辑单元格样式
     *
     * @param cellStyle
     */
    void editStyle(CellStyle cellStyle,Workbook workbook);

    /**
     * 编辑字体样式
     *
     * @param font
     */
    void editFont(Font font);


    /**
     * 样式默认单例
     */
    @Override
    default boolean isSingleton() {
        return true;
    }
}
