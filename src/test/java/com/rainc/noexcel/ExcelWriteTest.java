package com.rainc.noexcel;

import com.rainc.noexcel.builder.ExcelWriterBuilder;
import com.rainc.noexcel.write.ExcelWriter;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel读取测试
 *
 * @author rainc
 * @date 2021/12/22
 */
public class ExcelWriteTest {
    @Test
    public void writeFile() {
        //初始化要写入的数据
        List<TestEntity> testEntityList = new ArrayList<>();
        TestEntity testEntity = new TestEntity();
        testEntity.setDate(new Date());
        testEntity.setStr("测试文字");
        testEntity.setNumber(123L);
        testEntity.setColorEnum(ColorEnum.GOLD);
        for (int i = 0; i < 100; i++) {
            testEntityList.add(testEntity);
        }

        //写入
        ExcelWriterBuilder<TestEntity> builder = ExcelWriterBuilder.builder(TestEntity.class);
        try (ExcelWriter<TestEntity> excelWriter = builder.build()) {
            excelWriter.writeDataAndClose(testEntityList, new FileOutputStream("createFile.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeTemplate() {
        //构造模板
        try (ExcelWriter<TestEntity> excelWriter = ExcelWriterBuilder.builder(TestEntity.class).build();
             FileOutputStream fileOutputStream = new FileOutputStream("createTemplate.xls")) {
            excelWriter.writeTemplate(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
