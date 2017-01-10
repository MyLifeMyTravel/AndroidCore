package com.littlejie.core.util;

import android.webkit.MimeTypeMap;

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
            FileUtils.write(new File(path), data, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件的 mimeType
     *
     * @param path
     * @return
     */
    public static String getFileMimeType(String path) {
        return getMimeType("file://" + path);
    }

    /**
     * 获取 mimeType
     *
     * @param url
     * @return
     */
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
