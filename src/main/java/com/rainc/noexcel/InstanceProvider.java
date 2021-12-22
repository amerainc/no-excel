package com.rainc.noexcel;

import cn.hutool.core.util.ReflectUtil;

/**
 * 实例获取接口
 * @author rainc
 * @date 2021/9/1
 */
public interface InstanceProvider<T> {

    /**
     * 获取对象实例
     *
     * @return 获取的对象实例
     */
    default T getInstance() {
        if (isSingleton()) {
            return (T) this;
        } else {
            return (T) ReflectUtil.newInstance(this.getClass());
        }
    }

    /**
     * 是否是单例
     * @return 是否是单例
     */
    boolean isSingleton();
}
