package com.rainc.noexcel.util;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * excel行工具
 *
 * @author rainc
 * @date 2021/8/27
 */
public class RowUtil {
    /**
     * 获取指定范围的行,没有则创建
     *
     * @param start 起始行
     * @param end   结束行
     * @param sheet sheet
     * @return 行列表
     */
    public static List<Row> getRows(int start, int end, Sheet sheet) {
        return getRows(start, end, sheet,true);
    }

    /**
     * 获取指定范围的行
     *
     * @param start 起始行
     * @param end   结束行
     * @param sheet sheet
     * @param create 没有是否创建
     * @return 行列表
     */
    public static List<Row> getRows(int start, int end, Sheet sheet,boolean create) {
        List<Row> list = new ArrayList<>(end - start);
        for (int i = start; i <= end; i++) {
            list.add(RowUtil.getRow(i, sheet,create));
        }
        return list;
    }

    /**
     * 获取行，没有则创建
     *
     * @param index 获取行
     * @param sheet sheet
     * @return 行
     */
    public static Row getRow(int index, Sheet sheet) {
        return RowUtil.getRow(index,sheet,true);
    }

    /**
     * 获取行
     *
     * @param index 获取行
     * @param sheet sheet
     * @param create 没有是否创建
     * @return 行
     */
    public static Row getRow(int index, Sheet sheet,boolean create) {
        if (create){
            return Optional.ofNullable(sheet.getRow(index))
                    .orElseGet(()->sheet.createRow(index));
        }else{
            return sheet.getRow(index);
        }
    }

    /**
     * 自适应宽度
     *
     * @param row 需要自适应宽度的行
     */
    public static void autoWidth(Row row) {
        Sheet sheet = row.getSheet();
        int maxColumn = row.getLastCellNum();
        for (int i = 0; i < maxColumn; i++) {
            String value = CellUtil.getString(row.getCell(i));
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            sheet.setColumnWidth(i, 256 * bytes.length);
        }
    }

    /**
     * 是否是空行
     * @param row 检查行
     * @return 是否是空行
     */
    public static boolean isRowEmpty(Row row) {
        if (row==null){
            return true;
        }
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            //如果有不为空的单元格则返回false
            if (!StrUtil.isEmpty(CellUtil.getString(cell))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是空行
     * @param row 检查行
     * @return 是否是空行
     */
    public static boolean isRowNotEmpty(Row row) {
        return !isRowEmpty(row);
    }
}
