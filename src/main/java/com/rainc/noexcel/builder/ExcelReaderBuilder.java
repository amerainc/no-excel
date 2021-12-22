package com.rainc.noexcel.builder;

import com.rainc.noexcel.read.ExcelReader;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.InputStream;

/**
 * ExcelReader建造者
 *
 * @author rainc
 * @date 2021/8/31
 */
public class ExcelReaderBuilder<T> extends BaseExcelBuilder<T, ExcelReader<T>, ExcelReaderBuilder<T>> {
    public ExcelReaderBuilder(Class<T> clz) {
        super(clz);
    }

    public static <T> ExcelReaderBuilder<T> builder(Class<T> clz) {
        return new ExcelReaderBuilder<>(clz);
    }

    /**
     * 生成一个ExcelReader
     * @param inputStream excel文件输入流
     * @return ExcelReader
     */
    public ExcelReader<T> build(InputStream inputStream){
        return this.build(new ExcelReader<>(inputStream, this.clz));
    }

    /**
     * 生成一个ExcelReader
     * @param file excel文件
     * @return ExcelReader
     */
    public ExcelReader<T> build(File file) {
        return this.build(new ExcelReader<>(file, this.clz));
    }

    /**
     * 生成一个ExcelReader
     * @param workbook excel工作簿
     * @return ExcelReader
     */
    public ExcelReader<T> build(Workbook workbook) {
        return this.build(new ExcelReader<>(workbook, this.clz));
    }

    @Override
    protected ExcelReader<T> build(ExcelReader<T> excelReader) {
        return super.build(excelReader);
    }
}
