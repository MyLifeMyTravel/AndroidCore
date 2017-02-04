package com.littlejie.core.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * 文件管理类，如文件读写，请使用 Apache 的 commons-io
 * Created by littlejie on 2016/12/1.
 */

public class FileUtil {

    private static final String[][] MIME_TYPE = new String[][]{
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-JavaScript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    private static Map<String, String> mMimeTypeMap = new HashMap<>();

    static {
        for (int i = 0; i < MIME_TYPE.length; i++) {
            mMimeTypeMap.put(MIME_TYPE[i][0], MIME_TYPE[i][1]);
        }
    }

    /**
     * 根据后缀获取对应的mimeType
     *
     * @param suffix
     * @return
     */
    public static String getMimeTypeBySuffix(String suffix) {
        return mMimeTypeMap.get(suffix);
    }

    public static void write(String path, String data) {
        try {
            FileUtils.write(new File(path), data, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件的mimeType
     *
     * @param file
     * @return
     */
    public static String getMimeType(File file) {
        //returns the mime type for the given file or null iff there is none
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(file.getName()));
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

    public static Uri getFileUri(File file) {
        return Uri.fromFile(file);
    }

    /**
     * 获取 mimeType
     * 如果mimeType为null，分享的时候可能出现没有可以处理该Intent的app
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
        if (TextUtils.isEmpty(type)) {
            int lastIndex = url.lastIndexOf(".");
            if (lastIndex == -1) {
                return type;
            }
            String suffix = url.substring(lastIndex);
            type = mMimeTypeMap.get(suffix);
        }
        return type;
    }

    /**
     * 根据 Uri 获取对应文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPathFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        //如果uri的scheme为file，则直接返回path
        String scheme = uri.getScheme();
        if (scheme.equals("file")) {
            return uri.getPath();
        }
        String[] projection = new String[]{MediaStore.Files.FileColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            cursor.close();
            return path;
        }
        return null;
    }

    /**
     * 获取文件的Uri
     *
     * @param file
     * @return
     */
    public static Uri getUriFromFile(File file) {
        return Uri.fromFile(file);
    }

    public enum FileType {

        DIRECTORY, MISC_FILE, AUDIO, IMAGE, VIDEO, DOC, PPT, XLS, PDF, TXT, ZIP, APK;

        public static FileType getFileType(File file) {
            if (file.isDirectory())
                return FileType.DIRECTORY;

            String mime = getMimeType(file);

            if (mime == null)
                return FileType.MISC_FILE;

            if (mime.startsWith("audio"))
                return FileType.AUDIO;

            if (mime.startsWith("image"))
                return FileType.IMAGE;

            if (mime.startsWith("video"))
                return FileType.VIDEO;

            if (mime.startsWith("application/ogg"))
                return FileType.AUDIO;

            if (mime.startsWith("application/msword")
                    || mime.startsWith("application/vnd.ms-word")
                    || mime.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml"))
                return FileType.DOC;

            if (mime.startsWith("application/vnd.ms-powerpoint")
                    || mime.startsWith("application/vnd.openxmlformats-officedocument.presentationml"))
                return FileType.PPT;

            if (mime.startsWith("application/vnd.ms-excel")
                    || mime.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml"))
                return FileType.XLS;

            if (mime.startsWith("application/pdf"))
                return FileType.PDF;

            if (mime.startsWith("text"))
                return FileType.TXT;

            if (mime.startsWith("application/zip"))
                return FileType.ZIP;

            if (mime.startsWith("application/vnd.android.package-archive"))
                return APK;

            return FileType.MISC_FILE;
        }
    }
}
