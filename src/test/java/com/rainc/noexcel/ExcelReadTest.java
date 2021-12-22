package com.rainc.noexcel;

import com.rainc.noexcel.builder.ExcelReaderBuilder;
import com.rainc.noexcel.read.ExcelReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

/**
 * excel读取测试
 * @author rainc
 * @date 2021/12/22
 */
public class ExcelReadTest {
    @Test
    public void readFile() {
        String path = getClass().getResource("/").getPath();
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path + "readFile.xls"))) {
            List<TestEntity> testEntities = excelReader.readData();
            System.out.println(testEntities);
        }
    }
}
