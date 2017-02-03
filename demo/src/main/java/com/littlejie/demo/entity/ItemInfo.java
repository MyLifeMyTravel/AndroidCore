package com.littlejie.demo.entity;

/**
 * Demo 专用，保存 ListView 中 Item 的描述和对应的 Activity 类
 * Created by littlejie on 16/9/10.
 */
//Todo 废弃 ItemInfo， 默认使用 Description 注解，item 的描述从注解中读取
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
