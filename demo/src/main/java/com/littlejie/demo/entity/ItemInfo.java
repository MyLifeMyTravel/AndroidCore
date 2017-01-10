package com.littlejie.demo.entity;

/**
 * Created by littlejie on 16/9/10.
 */
public class ItemInfo {
    private String item;
    private Class clz;

    public ItemInfo(String item, Class clz) {
        this.item = item;
        this.clz = clz;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }

    @Override
    public String toString() {
        return item;
    }
}
