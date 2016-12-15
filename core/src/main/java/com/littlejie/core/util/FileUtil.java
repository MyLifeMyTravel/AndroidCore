package com.littlejie.core.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 文件管理类，如文件读写，请使用 Apache 的 commons-io
 * Created by littlejie on 2016/12/1.
 */

public class FileUtil {

    public static void write(String path, String data) {
        try {
            FileUtils.write(new File(path),data, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
