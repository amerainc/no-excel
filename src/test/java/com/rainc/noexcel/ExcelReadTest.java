package com.rainc.noexcel;

import com.rainc.noexcel.builder.ExcelReaderBuilder;
import com.rainc.noexcel.read.ExcelReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * excel读取测试
 * @author rainc
 * @date 2021/12/22
 */
public class ExcelReadTest {
    @Test
    public void readFile() {
        String path = getClass().getResource("/").getPath()+"readFile.xls";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path))) {
            List<TestEntity> testEntities = excelReader.readData();
            for (TestEntity testEntity : testEntities) {
                System.out.println(testEntity);
            }
        }
    }
    @Test
    public void readFile2() {
        String path = getClass().getResource("/").getPath()+"readFile.xls";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path))) {
           excelReader.readData(testEntity -> {
               System.out.println(testEntity);
           });
        }
    }

    @Test
    public void readFile3() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        String path = getClass().getResource("/").getPath()+"readFile.xls";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path))) {
            //这里将数据分为五片，并交由五个线程处理
            excelReader.readDataConcurrent(testEntity -> {
                System.out.println(testEntity);
            },executorService,5);
        }
    }
}
