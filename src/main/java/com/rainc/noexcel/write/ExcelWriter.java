package com.rainc.noexcel.write;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.rainc.noexcel.BaseExcel;
import com.rainc.noexcel.convert.CascadeProvider;
import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.convert.SelectProvider;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import com.rainc.noexcel.style.StyleProvider;
import com.rainc.noexcel.util.CellUtil;
import com.rainc.noexcel.util.ExcelUtil;
import com.rainc.noexcel.util.RequireUtil;
import com.rainc.noexcel.util.RowUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * excel导出类
 *
 * @author rainc
 * @date 2021/8/6
 */
@Getter
@Setter
public class ExcelWriter<T> extends BaseExcel<T> {
    /**
     * 隐藏的sheet用于存储下拉框信息
     */
    private Sheet hiddenSheet;
    /**
     * 是否需要下拉框
     */
    private boolean select=true;
    /**
     * 是否是xlsx格式
     */
    private boolean xlsx;
    /**
     * 数据样式
     */
    private CellStyle dataStyle;


    public ExcelWriter(Class<T> clz) {
        this(false, clz);
    }

    public ExcelWriter(boolean isXlsx, Class<T> clz) {
        super(ExcelUtil.createWorkbook(isXlsx).createSheet(), clz);
        this.xlsx=isXlsx;
    }


    @Override
    public void init() {
        super.init();
        initSheet();
        initTitle();
        initHead();
        initSelect();
        initData();
    }

    /**
     * 初始化下拉菜单
     */
    private void initSelect() {
        if (!isSelect()){
            return;
        }
        for (ExcelFieldMeta excelFieldMeta : this.excelFieldMetaList) {
            //生成级联就不生成普通下拉框了
            FieldConverter converter = excelFieldMeta.getConverter();
            if (converter instanceof CascadeProvider){
                CascadeProvider cascadeProvider= (CascadeProvider) converter;
                Map<String, List<String>> cascade = cascadeProvider.cascade();
                if (!CollectionUtil.isEmpty(cascade)){
                    this.initCascade(cascade,excelFieldMeta);
                }
                continue;
            }
            //生成下拉框数据
            if (converter instanceof SelectProvider) {
                SelectProvider dictConverter = (SelectProvider) converter;
                List<String> list = dictConverter.select();

                if (!CollectionUtil.isEmpty(list)) {
                    this.initSelectData(list, excelFieldMeta.getSort());
                }
            }
        }
    }

    private void initCascade(Map<String, List<String>> cascade,ExcelFieldMeta excelFieldMeta) {
        Integer cellIndex = excelFieldMeta.getSort();
        //公式模板
        String listFormulaTemplate = "{hiddenSheetName}!${cellIndex}${start}:${cellIndex}${end}";
        AtomicInteger index=new AtomicInteger(0);
        cascade.forEach((key,value)->{
            int start=index.get();
            //写入下拉框数据
            if (CollectionUtil.isEmpty(value)) {
                return;
            }
            for (String s : value) {
                Row row = RowUtil.getRow(index.getAndIncrement(), this.hiddenSheet);
                Cell cell = CellUtil.getCell(cellIndex, row);
                cell.setCellValue(s);
            }

            ///创建下拉框校验
            Name name = this.workbook.createName();
            name.setNameName(key);
            Map<String, String> map = new HashMap<>((int)((float)3 / 0.75F + 1.0F));
            map.put("hiddenSheetName",this.hiddenSheet.getSheetName());
            map.put("cellIndex",ExcelUtil.index2Col(cellIndex));
            map.put("start",String.valueOf(start+1));
            map.put("end",String.valueOf(index.get()));
            String listFormula = StrUtil.format(listFormulaTemplate,map);
            name.setRefersToFormula(listFormula);
        });
        int dependIndex=cellIndex-1;
        if (StrUtil.isNotBlank(excelFieldMeta.getCascadeDepend())){
            ExcelFieldMeta dependFieldMeta = CollectionUtil.findOne(this.excelFieldMetaList, meta -> excelFieldMeta.getCascadeDepend().equals(meta.getName()));
            if (dependFieldMeta!=null){
               dependIndex= dependFieldMeta.getSort();
            }
        }
        String formula="INDIRECT(OFFSET($"+ExcelUtil.index2Col(dependIndex)+"$1,ROW()-1,0,1,1))";

        //设置下拉框有效性校验
        this.setConstraint(formula, cellIndex);
    }


    /**
     * 初始化sheet参数
     */
    private void initSheet() {
        this.workbook.setSheetName(0, this.excelEntityMeta.getTitle());
        this.hiddenSheet = this.workbook.createSheet("hidden");
        this.workbook.setSheetHidden(1, true);
    }


    /**
     * 初始化标题
     */
    private void initTitle() {
        //如果不展示标题则不进行标题初始化
        if (!this.excelEntityMeta.isShowTitle()) {
            return;
        }
        int index = this.curIndex++;
        //合并标题的单元格
        this.sheet.addMergedRegion(new CellRangeAddress(index, index, 0, this.excelFieldMetaList.size() - 1));
        Row titleRow = RowUtil.getRow(index, sheet);
        //写入标题和样式
        StyleProvider titleStyle = this.getExcelEntityMeta().getTitleStyle();
        Cell cell = CellUtil.getCell(0, titleRow);
        cell.setCellStyle(titleStyle.getStyle(this));
        cell.setCellValue(this.getExcelEntityMeta().getTitle());
    }

    /**
     * 初始化表头
     */
    private void initHead() {
        //如果不展示表头则不进行表头初始化
        if (!this.excelEntityMeta.isShowHead()) {
            return;
        }
        int headIndex = curIndex++;
        //创建表头行
        Row headRow = RowUtil.getRow(headIndex, this.sheet);
        //普通表头样式
        CellStyle headStyle = this.excelEntityMeta.getHeadStyle().getStyle(this);
        //必填表头样式
        CellStyle headRequireStyle = this.excelEntityMeta.getHeadRequireStyle().getStyle(this);

        for (ExcelFieldMeta excelFieldMeta : this.excelFieldMetaList) {
            Integer sort = excelFieldMeta.getSort();
            boolean require = excelFieldMeta.isRequire();
            //写入表头信息和样式
            Cell cell = CellUtil.getCell(sort, headRow);
            cell.setCellStyle(require ? headRequireStyle : headStyle);
            cell.setCellValue(require ? RequireUtil.titleToRequireTitle(excelFieldMeta.getName()) : excelFieldMeta.getName());
        }
        //表头自适应宽度
        RowUtil.autoWidth(headRow);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //设置样式
        this.dataStyle=this.excelEntityMeta.getDataStyle().getStyle(this);
        for (ExcelFieldMeta excelFieldMeta : this.excelFieldMetaList) {
            this.sheet.setDefaultColumnStyle(excelFieldMeta.getSort(),this.dataStyle);
        }
    }

    /**
     * 初始化下拉框数据
     *
     * @param list      下拉框列表
     * @param cellIndex 单元格下标
     */
    private void initSelectData(List<String> list, int cellIndex) {
        //公式模板
        String listFormulaTemplate = "{hiddenSheetName}!${cellIndex}$1:${cellIndex}${size}";
        //写入下拉框数据
        for (int i = 0; i < list.size(); i++) {
            Row row = RowUtil.getRow(i, this.hiddenSheet);
            Cell cell = CellUtil.getCell(cellIndex, row);
            cell.setCellValue(list.get(i));
        }
        ///创建下拉框校验
        Map<String, String> map = new HashMap<>((int)((float)3 / 0.75F + 1.0F));
        map.put("hiddenSheetName",this.hiddenSheet.getSheetName());
        map.put("cellIndex",ExcelUtil.index2Col(cellIndex));
        map.put("size",String.valueOf(list.size()));
        String listFormula = StrUtil.format(listFormulaTemplate,map);
        //设置下拉框有效性校验
        this.setConstraint(listFormula, cellIndex);
    }

    /**
     * 设置有效性
     *
     * @param formula 下拉框数据
     * @param cellIndex  单元格下表
     */
    private void setConstraint(String formula, int cellIndex) {
        ///创建下拉框校验
        DataValidationHelper dataValidationHelper = this.sheet.getDataValidationHelper();
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(this.curIndex, isXlsx()?1048575:65535, cellIndex, cellIndex);
        // 数据有效性对象
        DataValidationConstraint constraint = dataValidationHelper.createFormulaListConstraint(formula);
        DataValidation validation = dataValidationHelper.createValidation(constraint, cellRangeAddressList);
        validation.createErrorBox("Error", "请选择或输入有效的选项，或下载最新模版重试！");
        validation.setShowErrorBox(true);
        this.sheet.addValidationData(validation);
    }


    /**
     * 写入数据
     *
     * @param data 数据
     */
    public ExcelWriter<T> writeData(List<T> data) {
        this.checkClosed();
        //判空
        if (CollectionUtil.isEmpty(data)) {
            return this;
        }
        //数据量限制
        if (data.size()>this.excelEntityMeta.getMaxSize()){
            throw new NoExcelException("数据量超出当前限制"+this.excelEntityMeta.getMaxSize()+"条");
        }
        //写入数据
        for (T t : data) {
            Row row = this.sheet.createRow(this.curIndex++);
            this.writeRowData(t, row);
        }
        return this;
    }

    /**
     * 写入行数据
     *
     * @param t   数据
     * @param row 行
     */
    private void writeRowData(T t, Row row) {
        for (ExcelFieldMeta excelFieldMeta : this.excelFieldMetaList) {
            Cell cell = row.createCell(excelFieldMeta.getSort());
            cell.setCellValue(excelFieldMeta.getExcelData(t));
            //xlsx需要写入数据后设置样式
            if (xlsx){
                cell.setCellStyle(this.dataStyle);
            }
        }
    }

    /**
     * 写数据并刷新
     *
     * @param data         需要写入的数据
     * @param outputStream 输出流
     */
    public void writeData(List<T> data, OutputStream outputStream) {
        this.writeData(data);
        this.flushData(outputStream);
    }
    /**
     * 写数据刷新并关闭输出流
     *
     * @param data         需要写入的数据
     * @param outputStream 输出流
     */
    public void writeDataAndClose(List<T> data, OutputStream outputStream) {
        this.writeData(data);
        this.flushData(outputStream);
        IoUtil.close(outputStream);
    }
    /**
     * 输出模板
     *
     * @param outputStream 输出流
     */
    public void writeTemplate(OutputStream outputStream) {
        this.writeData(Collections.singletonList(ReflectUtil.newInstance(this.clz)), outputStream);
    }

    /**
     * 将数据刷入流中
     *
     * @param outputStream 输出流
     */
    public void flushData(OutputStream outputStream) {
        this.checkClosed();
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new NoExcelException("文件输出失败",e);
        }
    }


    @Override
    protected void checkClosed() {
        if (this.isClosed) {
            throw new NoExcelException("ExcelReader 已经关闭");
        }
    }
}
