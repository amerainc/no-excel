package com.rainc.noexcel;
public enum  TrlAlertColorEnum {

    /**
     * 红色
     */
    RED("RED","红色"),

    /**
     * 金黄色
     */
    GOLD("GOLD","金黄色"),

    /**
     * 黄色
     */
    YELLOW("YELLOW","黄色");
    String value;
    String i18n;
    TrlAlertColorEnum( String value,String i18n){
        this.value = value;
        this.i18n = i18n;
    }

    public String getValue() {
        return value;
    }

    public String getI18n() {
        return i18n;
    }
}
