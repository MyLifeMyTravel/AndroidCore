package com.littlejie.demo.entity;

import com.google.gson.annotations.Expose;

/**
 * 测试 Gson 序列号和反序列化字段过滤
 * Created by littlejie on 2017/1/10.
 */

public class TestInfo {

    @Expose
    private String path;
    private String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
