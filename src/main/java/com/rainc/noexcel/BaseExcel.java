package com.rainc.noexcel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.func.Func1;
import com.rainc.noexcel.convert.FieldConverterHelper;
import com.rainc.noexcel.meta.ExcelEntityMeta;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import com.rainc.noexcel.util.ExcelMetaUtil;
import com.rainc.noexcel.util.MethodUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.Closeable;
import java.util.List;

/**
 * excel基类
 *
 * @author rainc
 * @date 2021/8/6
 */
@Getter
@Setter
public abstract class BaseExcel<T> implements Closeable {

    /**
     * 工作簿
     */
    protected Workbook workbook;
    /**
     * 当前sheet
     */
    protected Sheet sheet;
    /**
     * 当前所在行
     */
    protected int curIndex;
    /**
     * 是否已经关闭
     */
    protected boolean isClosed;
    /**
     * 是否已经初始化
     */
    protected boolean isInit;
    /**
     * excel注解信息
     */
    protected ExcelEntityMeta excelEntityMeta;
    /**
     * excel字段信息列表
     */
    protected List<ExcelFieldMeta> excelFieldMetaList;
    /**
     * excel导入导出类
     */
    protected Class<T> clz;

    protected BaseExcel(Sheet sheet, Class<T> clz) {
        this(sheet.getWorkbook(),sheet,clz);
    }

    protected BaseExcel(Workbook workbook, Sheet sheet, Class<T> clz) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.clz = clz;
        //解析类
        this.analyseClass();
    }

    /**
     * 初始化
     */
    protected void doInit() {
        if (this.isInit) {
            return;
        }
        //初始化
        init();
        this.isInit = true;
    }

    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 解析类信息
     */
    protected void analyseClass() {
        this.excelEntityMeta = ExcelMetaUtil.getExcelEntityMeta(this.clz);
        this.excelFieldMetaList = ExcelMetaUtil.getExcelFieldMeta(this.clz);
        for (int i = 0; i < this.excelFieldMetaList.size(); i++) {
            ExcelFieldMeta excelFieldMeta = this.excelFieldMetaList.get(i);
            //将排序置为list中的下标
            excelFieldMeta.setSort(i);
        }
    }


    /**
     * 忽略信息行
     *
     * @param fieldName 忽略行的属性名
     */
    protected void ignoreWithFieldName(String fieldName) {
        checkClosed();
        this.excelFieldMetaList.removeIf(excelFieldMeta -> excelFieldMeta.getFieldName().equals(fieldName));
    }

    /**
     * 判断是否关闭,关闭则抛出异常
     */
    protected abstract void checkClosed();

    /**
     * 忽略信息行
     *
     * @param func1 忽略行的get方法
     */
    protected  <F> void ignoreWithFieldName(Func1<F, ?> func1) {
        String fieldName = MethodUtil.getFieldNameWithGetter(func1);
        this.ignoreWithFieldName(fieldName);
    }

    /**
     * 忽略错误信息列
     */
    protected void ignoreErrMsg() {
        this.ignoreWithFieldName("errMsg");
    }

    /**
     * 关闭excel
     */
    @Override
    public void close() {
        IoUtil.close(this.workbook);
        this.sheet = null;
        this.workbook = null;
        this.isClosed = true;
    }
}
