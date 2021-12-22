package com.rainc.noexcel.convert;

import com.rainc.noexcel.InstanceProvider;
import com.rainc.noexcel.meta.ExcelFieldMeta;
import com.rainc.noexcel.util.GenericUtil;

/**
 * 字段转换器
 *
 * @author rainc
 * @date 2021/8/6
 */
public interface FieldConverter<T> extends InstanceProvider<FieldConverter<?>> {
    /**
     * excel数据转换成对应字段值
     *
     * @param excelData excel数据
     * @return 字段值
     */
    T parseToField(String excelData);

    /**
     * 将字段值转换为excel数据
     *
     * @param fieldData 字段值
     * @return excel数据
     */
    String parseToExcelData(T fieldData);

    /**
     * 初始化数据
     *
     * @param excelFieldMeta excel字段信息
     */
    default void initData(ExcelFieldMeta excelFieldMeta) {
    }

    /**
     * 默认匹配实现类的第一个泛型
     *
     * @param excelFieldMeta excel字段信息
     * @return 是否匹配成功
     */
    default boolean match(ExcelFieldMeta excelFieldMeta) {
        Class<?> firstGenericType = GenericUtil.getFirstGenericType(this.getClass());
        return firstGenericType != null && firstGenericType.isAssignableFrom(excelFieldMeta.getFieldClz());
    }

    /**
     * converter匹配优先级 越小越优先(目前只有默认匹配器时有效)
     *
     * @return 匹配优先级
     */
    default int order() {
        return 0;
    }

    /**
     * 是否是单例
     * 默认为否
     * @return 是否单例
     */
    @Override
    default boolean isSingleton(){
        return false;
    }
}
