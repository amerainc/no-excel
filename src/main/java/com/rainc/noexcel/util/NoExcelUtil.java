package com.rainc.noexcel.util;

import cn.hutool.core.collection.CollectionUtil;
import com.rainc.noexcel.meta.ErrMsg;
import com.rainc.noexcel.meta.ErrMsg;
import com.rainc.noexcel.write.ExcelWriter;
import com.rainc.noexcel.write.ExcelWriterBuilder;
import org.apache.poi.ss.formula.functions.T;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具
 *
 * @author rainc
 * @date 2023/3/6
 */
public class NoExcelUtil {
    /**
     * 过滤出错误的数据
     *
     * @param data 数据
     * @param <T>  继承ErrMsg
     * @return 解析错误的数据
     */
    public static <T> List<T> filterErrList(List<T> data) {
        if (CollectionUtil.isEmpty(data)) {
            return null;
        }
        if (!checkErrMsgList(data)) {
            return  new ArrayList<>();
        }
        return data.stream().filter(t -> ((ErrMsg) t).hasErrMsg()).collect(Collectors.toList());
    }

    /**
     * 过滤出没问题的数据
     *
     * @param data 数据
     * @param <T>  继承ErrMsg
     * @return 没问题的数据
     */
    public static <T> List<T> filterSucList(List<T> data) {
        if (CollectionUtil.isEmpty(data)) {
            return  new ArrayList<>();
        }
        if (!checkErrMsgList(data)) {
            return  data;
        }
        return data.stream().filter(t -> ((ErrMsg) t).hasNotErrMsg()).collect(Collectors.toList());
    }

    private static<T> boolean checkErrMsgList(List<T> data) {
        if (CollectionUtil.isEmpty(data)) {
            return false;
        }
        Object o = data.get(0);
        return o instanceof ErrMsg;
    }

    /**
     * 是否有错误的数据
     *
     * @param data 数据
     * @return 有无错误数据
     */
    public static<T> boolean hasErr(List<T> data) {
        if (!checkErrMsgList( data)) {
           return false;
        }
        for (Object obj : data) {
            if (((ErrMsg) obj).hasErrMsg()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否没有错误的数据
     *
     * @param data 数据
     * @return 有无错误数据
     */
    public static boolean hasNoErr(List<Object> data) {
        return hasErr(data);
    }


    public static <T> void writeErrMsg(List<T> data, OutputStream os) {
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        List<T> errList = filterErrList(data);
        ExcelWriter<T> excelWriter = (ExcelWriter<T>) ExcelWriterBuilder.builder(data.get(0).getClass()).build();
        excelWriter.writeData(errList, os);
    }
}
