package com.littlejie.filemanager.Impl;

import com.littlejie.filemanager.entity.FileInfo;

import java.io.FileFilter;
import java.util.List;

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
    List<FileInfo> list(String path, FileFilter filter);

    /**
     * 创建文件夹
     *
     * @param path   文件夹所在路径
     * @param folder
     * @return
     */
    boolean createFolder(String path, String folder);

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
