package com.rainc.noexcel;

import cn.hutool.core.util.ReflectUtil;
import com.rainc.noexcel.convert.FieldConverterHelper;
import com.rainc.noexcel.convert.impl.DefaultFieldConverter;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import com.rainc.noexcel.read.ExcelReaderBuilder;
import com.rainc.noexcel.util.NoExcelUtil;
import com.rainc.noexcel.read.ExcelReader;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * excel读取测试
 *
 * @author rainc
 * @date 2021/12/22
 */
public class ExcelReadTest {
    @Test
    public void readFile() {
        String path = "readFile.xls";
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
        String path = "readFile.xls";
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
        String path = "readFile.xls";
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        try (ExcelReader<TestEntity> excelReader = builder.build(new File(path))) {
            //这里将数据分为五片，并交由五个线程处理
            excelReader.readDataConcurrent(testEntity -> {
                System.out.println(testEntity);
            }, executorService, 5);
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
            NoExcelUtil.writeErrMsg(testEntities, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read() {
        ExcelReaderBuilder<A> builder = ExcelReaderBuilder.builder(A.class);
        Field a = ReflectUtil.getField(A.class, "a");
        ExcelFieldMeta excelFieldMeta = new ExcelFieldMeta();
        excelFieldMeta.setFieldName(a.getName());
        excelFieldMeta.setField(a);
        excelFieldMeta.setSort(0);
        excelFieldMeta.setName("必填选项");
        excelFieldMeta.setConverter(DefaultFieldConverter.class);
        excelFieldMeta.setConverterInstance(FieldConverterHelper.getFieldConverter(excelFieldMeta));
        excelFieldMeta.setRequire(true);
        builder.setMaxSize(Integer.MAX_VALUE)
                .setHasTitle(true)
                .setHasHead(true)
                .setTitle("测试")
                .addExcelFieldMeta(excelFieldMeta);
        try (ExcelReader<A> reader = builder
                .build(new File("/Users/rainc/Documents/project/no-excel/readFile.xls"))) {
            System.out.println(reader.readData());
        }
    }

    @Data
    static class A {
        private String a;

        @Override
        public String toString() {
            return "A{" +
                    "a='" + a + '\'' +
                    '}';
        }
    }
}
