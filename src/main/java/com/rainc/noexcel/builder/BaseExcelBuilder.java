package com.rainc.noexcel.builder;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.lang.func.Func1;
import com.rainc.noexcel.BaseExcel;
import com.rainc.noexcel.meta.BaseErrMsg;
import com.rainc.noexcel.meta.ExcelEntityMeta;
import com.rainc.noexcel.style.StyleProvider;
import com.rainc.noexcel.util.MethodUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;

/**
 * Excel建造者模板
 *
 * @author rainc
 * @date 2021/8/31
 */
@Getter
@Setter
public abstract class BaseExcelBuilder<T, Instance extends BaseExcel<T>, Builder extends BaseExcelBuilder<T, Instance, Builder>> {
    /**
     * excel导入导出类
     */
    protected Class<T> clz;
    /**
     * 所有忽略的行
     */
    protected Set<String> ignoreFieldNameSet;
//----------------- ExcelEntityMeta-------------------
    /**
     * 最大行数限制
     */
    protected Integer maxSize;
    /**
     * 是否显示标题
     */
    protected Boolean showTitle;
    /**
     * excel导出时的标题名
     */
    protected String title;
    /**
     * 标题样式
     */
    protected StyleProvider titleStyle;
    /**
     * 必填内容的表头样式
     */
    protected StyleProvider headRequireStyle;
    /**
     * 表头样式
     */
    protected StyleProvider headStyle;
    /**
     * 数据样式
     */
    protected StyleProvider dataStyle;

    protected int curIndex;


    public BaseExcelBuilder(Class<T> clz) {
        this.clz = clz;
        this.ignoreFieldNameSet = new ConcurrentHashSet<>();
    }

    /**
     * 忽略信息行
     *
     * @param fieldName 忽略行的属性名
     */
    public Builder ignoreWithFieldName(String fieldName) {
        this.ignoreFieldNameSet.add(fieldName);
        return self();
    }

    /**
     * 忽略信息行
     *
     * @param func1 忽略行的get方法
     */
    public <F> Builder ignoreWithFieldName(Func1<F, ?> func1) {
        String fieldName = MethodUtil.getFieldNameWithGetter(func1);
        return this.ignoreWithFieldName(fieldName);
    }

    /**
     * 忽略错误信息行
     */
    public Builder ignoreErrMsg() {
        return this.ignoreWithFieldName(BaseErrMsg::getErrMsg);
    }

    /**
     * 返回自身
     *
     * @return 自身
     */
    protected Builder self() {
        return (Builder) this;
    }

    /**
     * 构造
     *
     * @param baseExcel 实现类创建的实例
     * @return 构造完成后的对象
     */
    protected Instance build(Instance baseExcel) {
        initIgnoreField(baseExcel);
        initExcelEntity(baseExcel);
        baseExcel.init();
        return baseExcel;
    }

    /**
     * 初始化excelEntity中的所有参数
     *
     * @param baseExcel 实现类创建的实例
     */
    protected void initExcelEntity(Instance baseExcel) {
        ExcelEntityMeta excelEntityMeta = baseExcel.getExcelEntityMeta();
        Optional.ofNullable(this.maxSize).ifPresent(excelEntityMeta::setMaxSize);
        Optional.ofNullable(this.showTitle).ifPresent(excelEntityMeta::setShowTitle);
        Optional.ofNullable(this.title).ifPresent(excelEntityMeta::setTitle);
        Optional.ofNullable(this.titleStyle).ifPresent(excelEntityMeta::setTitleStyle);
        Optional.ofNullable(this.headStyle).ifPresent(excelEntityMeta::setHeadStyle);
        Optional.ofNullable(this.headRequireStyle).ifPresent(excelEntityMeta::setHeadRequireStyle);
        Optional.ofNullable(this.dataStyle).ifPresent(excelEntityMeta::setDataStyle);
    }

    /**
     * 初始化忽略属性
     *
     * @param baseExcel 实现类创建的实例
     */
    private void initIgnoreField(Instance baseExcel) {
        this.ignoreFieldNameSet.forEach(baseExcel::ignoreWithFieldName);
    }

    public Class<T> getClz() {
        return clz;
    }

    public Builder setClz(Class<T> clz) {
        this.clz = clz;
        return (Builder) this;
    }

    public Set<String> getIgnoreFieldNameSet() {
        return ignoreFieldNameSet;
    }

    public Builder setIgnoreFieldNameSet(Set<String> ignoreFieldNameSet) {
        this.ignoreFieldNameSet = ignoreFieldNameSet;
        return (Builder) this;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public Builder setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
        return (Builder) this;
    }

    public Boolean getShowTitle() {
        return showTitle;
    }

    public Builder setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
        return (Builder) this;
    }

    public String getTitle() {
        return title;
    }

    public Builder setTitle(String title) {
        this.title = title;
        return (Builder) this;
    }

    public StyleProvider getTitleStyle() {
        return titleStyle;
    }

    public Builder setTitleStyle(StyleProvider titleStyle) {
        this.titleStyle = titleStyle;
        return (Builder) this;
    }

    public StyleProvider getHeadRequireStyle() {
        return headRequireStyle;
    }

    public Builder setHeadRequireStyle(StyleProvider headRequireStyle) {
        this.headRequireStyle = headRequireStyle;
        return (Builder) this;
    }

    public StyleProvider getHeadStyle() {
        return headStyle;
    }

    public Builder setHeadStyle(StyleProvider headStyle) {
        this.headStyle = headStyle;
        return (Builder) this;
    }

    public StyleProvider getDataStyle() {
        return dataStyle;
    }

    public Builder setDataStyle(StyleProvider dataStyle) {
        this.dataStyle = dataStyle;
        return (Builder) this;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public Builder setCurIndex(int curIndex) {
        this.curIndex = curIndex;
        return (Builder) this;
    }
}