package com.rainc.noexcel;

import com.rainc.noexcel.read.ExcelReaderBuilder;
import com.rainc.noexcel.util.NoExcelUtil;
import com.rainc.noexcel.read.ExcelReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * excel读取测试
 * @author rainc
 * @date 2021/12/22
 */
public class ExcelReadTest {
    @Test
    public void readFile() {
        String path = getClass().getResource("/").getPath()+"";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(getClass().getResourceAsStream("readFile.xls"))) {
            List<TestEntity> testEntities = excelReader.readData();
            for (TestEntity testEntity : testEntities) {
                System.out.println(testEntity);
            }
        }
    }
    @Test
    public void readFile2() {
        String path = getClass().getResource("/").getPath()+ "readFile.xls";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path))) {
           excelReader.readData(testEntity -> {
               System.out.println(testEntity);
           });
        }
    }

    @Test
    public void readFile3() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        String path = getClass().getResource("/").getPath()+ "readFile.xls";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path))) {
            //这里将数据分为五片，并交由五个线程处理
            excelReader.readDataConcurrent(testEntity -> {
                System.out.println(testEntity);
            },executorService,5);
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
    }

    @Test
    public void readerrorFile() {
        //读取excel
        String path = "readerrFile.xls";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        List<TestEntity> testEntities;
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path))) {
            testEntities = excelReader.readData();
        }
        //过滤成功的数据
        List<TestEntity> success = NoExcelUtil.filterSucList(testEntities);
        System.out.println(success);
        //将失败的数据重新导成excel
        try (FileOutputStream fileOutputStream = new FileOutputStream("errFile.xls")) {
            NoExcelUtil.writeErrMsg(testEntities,fileOutputStream);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
