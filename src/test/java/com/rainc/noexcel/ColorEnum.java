package com.rainc.noexcel;
public enum ColorEnum {
    RED("红色"),
    GOLD("金黄色"),
    YELLOW("黄色");
    String i18n;
    ColorEnum(String i18n){
        this.i18n = i18n;
    }
    public String getI18n() {
        return i18n;
    }
}
