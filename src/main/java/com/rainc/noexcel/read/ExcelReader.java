package com.rainc.noexcel.read;

import cn.hutool.core.util.ReflectUtil;
import com.rainc.noexcel.BaseExcel;
import com.rainc.noexcel.exception.NoExcelException;
import com.rainc.noexcel.meta.BaseErrMsg;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import com.rainc.noexcel.util.CellUtil;
import com.rainc.noexcel.util.ExcelUtil;
import com.rainc.noexcel.util.RequireUtil;
import com.rainc.noexcel.util.RowUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * excel导入类
 *
 * @author rainc
 * @date 2021/8/12
 */
public class ExcelReader<T> extends BaseExcel<T> {
    private List<Row> data;

    public ExcelReader(InputStream inputStream, Class<T> clz) {
        this(ExcelUtil.createWorkbook(inputStream), clz);
    }

    public ExcelReader(File file, Class<T> clz) {
        this(ExcelUtil.create(file), clz);
    }

    public ExcelReader(Workbook workbook, Class<T> clz) {
        super(workbook.getSheetAt(0), clz);
    }

    @Override
    public void init() {
        super.init();
        initTitle();
        initHead();
        initData();
    }


    /**
     * 初始化表头
     */
    private void initHead() {
        int headIndex = this.curIndex++;
        Row head = this.sheet.getRow(headIndex);
        Map<String, ExcelFieldMeta> excelFieldMetaMap = this.excelFieldMetaList
                .stream()
                //读取时先清空注解上的排序
                .peek(excelFieldMeta -> excelFieldMeta.setSort(null))
                .collect(Collectors.toMap(ExcelFieldMeta::getName, excelFieldMeta -> excelFieldMeta));

        for (int i = 0; i < head.getLastCellNum(); i++) {
            Cell cell = head.getCell(i);
            String value = cell.getStringCellValue();
            value = RequireUtil.requireTitleToTitle(value);
            final int sort = i;
            //通过excel的表头和字段顺序进行匹配
            excelFieldMetaMap.computeIfPresent(value, (key, excelFieldMeta) -> {
                excelFieldMeta.setSort(sort);
                return excelFieldMeta;
            });
        }
        //过滤不存在的元素并重新排序
        this.excelFieldMetaList = this.excelFieldMetaList.stream()
                .filter(excelFieldMeta -> excelFieldMeta.getSort() != null)
                .sorted(Comparator.comparing(ExcelFieldMeta::getSort))
                .collect(Collectors.toList());
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        Row row = this.sheet.getRow(this.curIndex);
        Cell cell = CellUtil.getCell(0, row);
        String value = CellUtil.getString(cell);
        //与标题不相等则认为没有标题
        if (!this.excelEntityMeta.getTitle().equals(value)) {
            return;
        }
        this.curIndex++;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int last = this.sheet.getLastRowNum();
        List<Row> rows = RowUtil.getRows(this.curIndex, last, this.sheet, false);
        this.data = rows.stream().filter(RowUtil::isRowNotEmpty).collect(Collectors.toList());
        if (this.excelEntityMeta.getMaxSize() < this.data.size()) {
            throw new NoExcelException("数据量超出当前限制" + this.excelEntityMeta.getMaxSize() + "条");
        }
        //忽略错误行的读取
        this.ignoreErrMsg();
    }

    /**
     * 读取数据
     *
     * @return 读取到的数据列表
     */
    public List<T> readData() {
       return readData(0,1);
    }

    /**
     * 读取数据
     * @param index 当前分片
     * @param total 总分片
     * @return 读取到的数据列表
     */
    public List<T> readData(int index,int total) {
        checkClosed();
        List<T> list = new ArrayList<>(this.data.size());
        readData(list::add,index,total);
        return list;
    }

    /**
     * 消费数据
     *
     * @param consumer 消费者
     */
    public void readData(Consumer<T> consumer) {
        readData(consumer,0,1);
    }

    /**
     * 消费数据
     *
     * @param consumer 消费者
     */
    public void readDataConcurrent(Consumer<T> consumer, Executor executor,int total) {
        for (int i = 0; i < total; i++) {
            int index = i;
            executor.execute(()-> readData(consumer, index,total));
        }
    }

    /**
     * 分片消费数据
     * @param consumer 消费者
     * @param index 当前分片
     * @param total 总分片
     */
    public void readData(Consumer<T> consumer,int index,int total) {
        checkClosed();
        List<Row> rows = this.data;
        int size = rows.size();
        int fragment = size / total;
        int start = fragment*index;
        int end = index==(total-1)?size:(fragment*(index+1));
        for (int i = start; i < end; i++) {
            T data = readData(rows.get(i));
            //消费数据
            consumer.accept(data);
        }
    }

    /**
     * 读取某行数据
     *
     * @param row 某行数据
     * @return
     */
    private T readData(Row row) {
        T data = createData();
        //如果需要异常初始化
        StringBuffer errMsg = null;

        boolean needErrMsg = data instanceof BaseErrMsg;
        //读取数据
        for (ExcelFieldMeta excelFieldMeta : this.excelFieldMetaList) {
            Cell cell = row.getCell(excelFieldMeta.getSort());
            try {
                String value = Optional.ofNullable(cell).map(CellUtil::getString).orElse("");
                excelFieldMeta.setExcelData(data, value);
            } catch (NoExcelException e) {
                if (needErrMsg) {
                    errMsg = appendErrMsg(errMsg, excelFieldMeta, e);
                }
            }
        }
        if (needErrMsg) {
            ((BaseErrMsg) data).setErrMsg(errMsg);
        }
        return data;
    }

    /**
     * 拼接错误信息
     *
     * @param errMsg         错误信息拼接器
     * @param excelFieldMeta 当前字段信息
     * @param e              异常信息
     */
    private StringBuffer appendErrMsg(StringBuffer errMsg, ExcelFieldMeta excelFieldMeta, NoExcelException e) {
        if (errMsg == null) {
            errMsg = new StringBuffer();
        }
        errMsg.append(excelFieldMeta.getName());
        errMsg.append(e.getMessage());
        errMsg.append(";\n");
        return errMsg;
    }

    /**
     * 生成一个数据实例
     *
     * @return 数据实例
     */
    private T createData() {
        return ReflectUtil.newInstance(this.clz);
    }

    /**
     * 判断是否关闭
     */
    @Override
    protected void checkClosed() {
        if (this.isClosed) {
            throw new NoExcelException("ExcelWriter 已经关闭");
        }
    }
}
