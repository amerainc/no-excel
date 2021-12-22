package com.rainc.noexcel.meta;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.rainc.noexcel.convert.FieldConverter;
import com.rainc.noexcel.exception.NoExcelException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.DataValidationConstraint;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * excel字段信息
 *
 * @author rainc
 * @date 2021/8/6
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ExcelFieldMeta {
    /**
     * 字段标题
     */
    String name;
    /**
     * 字段顺序
     */
    Integer sort;
    /**
     * 是否必填
     */
    boolean require;
    /**
     * 额外参数
     */
    String param;
    /**
     * 级联依赖字段
     */
    String cascadeDepend;
    /**
     * 字段转换器
     */
    FieldConverter converter;
    /**
     * 属性归属的类
     */
    Class<?> belongClz;
    /**
     * 字段名
     */
    String fieldName;
    /**
     * 字段
     */
    Field field;
    /**
     * 字段类
     */
    Class<?> fieldClz;
    /**
     * set方法
     */
    Method getMethod;
    /**
     * get方法
     */
    Method setMethod;
    /**
     * 下拉框数据
     */
    DataValidationConstraint constraint;

    /**
     * 获取字段属性并转换为excel展示数据
     *
     * @param object 当前转换对象
     * @return excel展示的数据
     */
    public String getExcelData(Object object) {
        if (this.belongClz != object.getClass()) {
            return null;
        }
        Object field = ReflectUtil.invoke(object, this.getMethod);
        if (field == null) {
            return "";
        }
        return this.getConverter().parseToExcelData(field);
    }

    /**
     * 转换excel值并赋值给属性
     *
     * @param object    当前赋值对象
     * @param excelData excel值
     */
    public void setExcelData(Object object, String excelData) {
        if (StrUtil.isBlank(excelData)) {
            if (require) {
                throw new NoExcelException("不能为空");
            } else {
                return;
            }
        }
        ReflectUtil.invoke(object, this.setMethod, this.getConverter().parseToField(excelData));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExcelFieldMeta that = (ExcelFieldMeta) o;
        return Objects.equals(name, that.name) && Objects.equals(param, that.param) && Objects.equals(cascadeDepend, that.cascadeDepend) && Objects.equals(belongClz, that.belongClz) && Objects.equals(fieldName, that.fieldName) && Objects.equals(field, that.field) && Objects.equals(fieldClz, that.fieldClz) && Objects.equals(getMethod, that.getMethod) && Objects.equals(setMethod, that.setMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, param, cascadeDepend, belongClz, fieldName, field, fieldClz, getMethod, setMethod);
    }
}
