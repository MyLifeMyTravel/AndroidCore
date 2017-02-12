package com.littlejie.filemanager.util;

import java.io.File;
import java.util.Comparator;

/**
 * Created by littlejie on 2017/2/12.
 */

public class CustomComparator implements Comparator<File> {

    @Override
    public int compare(File file1, File file2) {
        /**
         * 1.先比较文件夹 （文件夹在文件的顺序之上）
         * 2.以A-Z的字典排序
         * 3.比较文件夹和文件
         * 4.比较文件和文件夹
         */
        if (file1.isDirectory() && file2.isDirectory()) {
            return file1.getName().compareToIgnoreCase(file2.getName());
        } else {
            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;
            } else if (!file1.isDirectory() && file2.isDirectory()) {
                return 1;
            } else {
                return file1.getName().compareToIgnoreCase(file2.getName());
            }
        }
    }

}
