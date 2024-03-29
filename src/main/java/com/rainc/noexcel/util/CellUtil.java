package com.rainc.noexcel.util;

import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * excel单元格工具
 * @author rainc
 * @date 2021/8/27
 */
public class CellUtil {
    private static final DataFormatter DATA_FORMATTER = new DataFormatter();
    /**
     * 获取单元格的值为string类型
     *
     * @param cell 单元格
     * @return 值
     */
    public static String getString(Cell cell) {
        if (cell==null){
            return "";
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case FORMULA:
                return cell.getCellFormula();
            case NUMERIC :
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
            case STRING :
                return cell.getRichStringCellValue().getString();
            case BOOLEAN :
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case BLANK :
                return "";
            case ERROR:
                return FormulaError.forInt(cell.getErrorCellValue()).getString();
            default:
                throw new RuntimeException("Unexpected celltype (" + cellType + ")");
        }
    }

    /**
     * 获取单元格，没有则创建
     * @param index 获取单元格
     * @param row 行
     * @return 单元格
     */
    public static Cell getCell(int index, Row row) {
        return Optional.ofNullable(row.getCell(index))
                .orElseGet(()->row.createCell(index));
    }

    /**
     * 获取指定范围的单元格,没有则创建
     * @param start 起始单元格
     * @param end 结束单元格
     * @param row 行
     * @return 单元格列表
     */
    public static List<Cell> getCells(int start, int end, Row row) {
        List<Cell> list=new ArrayList<>(end-start);
        for (int i = start; i <= end; i++) {
          list.add(  CellUtil.getCell(i,row));
        }
        return list;
    }

    /**
     * 自适应宽度
     *
     * @param cell 需要自适应宽度的单元格所在列(需要有数据)
     */
    public static void autoWidth(Cell cell) {
        Sheet sheet = cell.getSheet();
        String value = CellUtil.getString(cell);
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        sheet.setColumnWidth(cell.getColumnIndex(), 256 * bytes.length);
    }
}
