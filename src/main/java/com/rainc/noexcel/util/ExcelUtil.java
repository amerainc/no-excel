package com.rainc.noexcel.util;

import com.rainc.noexcel.exception.NoExcelException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * excel工具
 * @author rainc
 * @date 2021/8/6
 */
public class ExcelUtil {
    /**
     * 创建工作簿
     *
     * @param isXlsx 是否为xlsx格式
     * @return 工作簿
     */
    public static Workbook createWorkbook(boolean isXlsx) {
        return isXlsx ? new XSSFWorkbook() : new HSSFWorkbook();
    }

    /**
     * 创建工作簿
     * @param inputStream excel文件的输入流
     * @return
     */
    public static Workbook createWorkbook(InputStream inputStream) {
        return createWorkbook(() -> WorkbookFactory.create(inputStream));
    }

    /**
     * 创建工作簿，包裹掉io异常
     * @param createWorkbook  workbook创建接口
     * @return
     */
    public static Workbook createWorkbook(CreateWorkbook createWorkbook) {
        try {
            return createWorkbook.create();
        } catch (IOException e) {
            e.printStackTrace();
            throw new NoExcelException("创建工作簿失败", e);
        }
    }

    public static Workbook create(File file) {
        return createWorkbook(()->WorkbookFactory.create(file));
    }

    /**
     * workbook创建接口
     */
    @FunctionalInterface
    interface CreateWorkbook {
        /**
         * 创建workbook
         * @return 创建的工作簿
         * @throws IOException io异常
         */
        Workbook create() throws IOException;
    }

    /**
     * 字母数量
     */
    private final static int LETTER_NUM = 26;

    /**
     * 转单元格index 'AA'
     *
     * @param index index
     * @return 单元格index
     */
    public static String index2Col(int index) {
        StringBuilder sb = new StringBuilder();
        for (int i = index + 1; i > 0; i /= LETTER_NUM) {
            sb.append((char) ('A' + i % LETTER_NUM - 1));
        }
        return sb.reverse().toString();
    }


}
