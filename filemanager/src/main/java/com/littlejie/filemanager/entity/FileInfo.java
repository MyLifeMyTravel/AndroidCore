package com.littlejie.filemanager.entity;

import java.io.File;

/**
 * Created by littlejie on 2016/12/28.
 */

public class FileInfo {

    protected String id;
    protected String path;
    protected String name;
    private long lastModify;
    protected int parent;
    private long size;
    protected File file;
    private String mimeType;

    public boolean isDirectory() {
        return file != null && file.isDirectory();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        file = new File(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", lastModify=" + lastModify +
                ", parent=" + parent +
                ", size=" + size +
                ", file=" + file +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}
