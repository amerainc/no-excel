package com.rainc.noexcel.meta;

import cn.hutool.core.clone.CloneSupport;
import com.rainc.noexcel.style.StyleProvider;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * excel实体信息
 *
 * @author rainc
 * @date 2021/8/6
 */
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ExcelEntityMeta extends CloneSupport<ExcelEntityMeta> {
    /**
     * excel导出时的标题名
     */
    String title;
    /**
     * excel是否有标题
     */
    boolean hasTitle;
    /**
     * excel是否有表头
     */
    boolean hasHead;
    /**
     * 读取写入最大长度
     */
    int maxSize;
    /**
     * 标题样式
     */
    StyleProvider titleStyle;
    /**
     * 必填内容的表头样式
     */
    StyleProvider headRequireStyle;
    /**
     * 表头样式
     */
    StyleProvider headStyle;
    /**
     * 数据样式
     */
    StyleProvider dataStyle;
}
