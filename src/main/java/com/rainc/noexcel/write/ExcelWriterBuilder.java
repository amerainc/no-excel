package com.rainc.noexcel.write;

import com.rainc.noexcel.BaseExcelBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.function.Consumer;

/**
 * excelWriter建造者
 *
 * @author rainc
 * @date 2021/8/31
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExcelWriterBuilder<T> extends BaseExcelBuilder<T, ExcelWriter<T>, ExcelWriterBuilder<T>> {
    private boolean xlsx;
    private boolean select=true;
    private Integer sheetIndex;

    public ExcelWriterBuilder(Class<T> clz) {
        super(clz);
    }


    public ExcelWriterBuilder(boolean xlsx, Class<T> clz) {
        super(clz);
        this.xlsx = xlsx;
    }

    public static <T>ExcelWriterBuilder<T> builder(Class<T> clz) {
        return new ExcelWriterBuilder<>(clz);
    }

    public static <T>ExcelWriterBuilder<T> builder(boolean isXlsx, Class<T> clz) {
        return new ExcelWriterBuilder<>(isXlsx, clz);
    }

    public ExcelWriter<T> build() {
        return this.build(new ExcelWriter<>(this.xlsx, this.clz));
    }

    public void buildAndWrite(Consumer<ExcelWriter<T>> consumer){
        try (ExcelWriter<T> excelWriter = this.build()) {
            consumer.accept(excelWriter);
        }
    }

    @Override
    protected ExcelWriter<T> build(ExcelWriter<T> excelWriter) {
        //初始化sheetIndex
        if (sheetIndex!=null){
            Sheet sheet = excelWriter.getWorkbook().getSheetAt(sheetIndex);
            excelWriter.setSheet(sheet);
        }
        excelWriter.setSelect(select);
        return super.build(excelWriter);
    }
}
