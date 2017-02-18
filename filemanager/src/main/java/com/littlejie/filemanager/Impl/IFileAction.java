package com.littlejie.filemanager.impl;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件操作接口
 * Created by littlejie on 2017/1/10.
 */

public interface IFileAction {

    /**
     * 列出当前路径下的所有文件
     *
     * @param path
     * @param filter
     * @return
     */
    File[] list(String path, FileFilter filter);

    /**
     * 创建文件夹
     *
     * @param path 文件夹所在路径
     * @param dir  文件夹名称
     * @return
     */
    boolean mkdirs(String path, String dir);

    /**
     * 移动文件
     *
     * @param src
     * @param dest
     * @return
     */
    boolean move(String src, String dest);

    /**
     * 拷贝文件
     *
     * @param src
     * @param dest
     * @return
     */
    boolean copy(String src, String dest);

    /**
     * 重命名文件，实质为移动文件
     *
     * @param src
     * @param dest
     * @return
     */
    boolean rename(String src, String dest);

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    boolean delete(String path);
}
