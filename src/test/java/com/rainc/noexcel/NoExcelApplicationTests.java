package com.rainc.noexcel;

import com.rainc.noexcel.builder.ExcelReaderBuilder;
import com.rainc.noexcel.builder.ExcelWriterBuilder;
import com.rainc.noexcel.read.ExcelReader;
import com.rainc.noexcel.write.ExcelWriter;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class NoExcelApplicationTests {

    @Test
    public void createTemplate() {
        ExcelWriterBuilder<TestEntity> builder = ExcelWriterBuilder.builder(TestEntity.class)
                .ignoreErrMsg();
        //构造模板
        try (ExcelWriter<TestEntity> excelWriter = builder.build();
             FileOutputStream fileOutputStream = new FileOutputStream("createTemplate.xls")) {
            excelWriter.writeTemplate(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createFile() {
        List<TestEntity> testEntityList = new ArrayList<>();
        TestEntity testEntity = new TestEntity();
        testEntity.setDate(null);
        testEntity.setStr("测试文字");
        testEntity.setNumber((short) 123);
        testEntity.setAnEnum(TrlAlertColorEnum.GOLD);
//        testEntity.setLevel("sred");
        for (int i = 0; i < 100; i++) {
            testEntityList.add(testEntity);
        }
        ExcelWriterBuilder<TestEntity> builder = ExcelWriterBuilder.builder(TestEntity.class)
                .setXlsx(true)
                .ignoreErrMsg();
        //构造模板
        try (ExcelWriter<TestEntity> excelWriter = builder.build();
             FileOutputStream fileOutputStream = new FileOutputStream("createFile.xlsx")) {
            excelWriter.writeData(testEntityList, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExcelWriterBuilder<TestEntity> builder2 = ExcelWriterBuilder.builder(TestEntity.class)
                .ignoreErrMsg();
        //构造模板
        try (ExcelWriter<TestEntity> excelWriter = builder2.build();
             FileOutputStream fileOutputStream = new FileOutputStream("createFile.xls")) {
            excelWriter.writeData(testEntityList, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFile() {
        ExcelReaderBuilder<TestEntity> builder = ExcelReaderBuilder.builder(TestEntity.class);
        for (int i = 0; i < 100; i++) {
            try (ExcelReader<TestEntity> excelReader = builder.build(new FileInputStream("createTemplate.xls"))) {
                final List<TestEntity> testEntityList = excelReader.readData();
                System.out.println(testEntityList);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
