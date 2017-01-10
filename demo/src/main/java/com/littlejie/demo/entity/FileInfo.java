package com.littlejie.demo.entity;

import com.littlejie.core.util.TimeUtil;

import java.io.File;

/**
 * Created by littlejie on 2016/12/28.
 */

public class FileInfo {

    private long id;
    private String path;
    private String name;
    private long modify;
    private int parent;
    private File file;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getModify() {
        return modify;
    }

    public void setModify(long modify) {
        this.modify = modify;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        if (file == null) {
            file = new File(path);
        }
        return "名称：" + name
                + "\n路径：" + path
                + "\n修改时间：" + TimeUtil.parse2TimeDetail(modify * 1000)
                + "\n是否为目录：" + (file != null && file.isDirectory() ? "是" : "否");
    }
}
