package com.littlejie.demo.modules.base.media;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.littlejie.demo.entity.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by littlejie on 2017/2/4.
 */

public class Test {

    public static final String TAG = Test.class.getSimpleName();
    private static final String[] DOCUMENT = new String[]{".doc", ".docx", "xls", "xlsx", "ppt", "pptx"};
    private static final Uri CONTENT_URI = MediaStore.Files.getContentUri("external");
    private static final String SPLIT = ";";
    private static String DOWNLOADS = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
            + Environment.DIRECTORY_DOWNLOADS;
    private static final File DOWNLOAD_FOLDER = new File(DOWNLOADS);

    public enum FilterType {
        SUFFIX, MIME_TYPE
    }

    public static List<FileInfo> getImageFolder(Context context) {
        Set<String> folder = getMediaFolder(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true);
        return getFileInfos(folder);
    }

    public static List<FileInfo> getAudioFolder(Context context) {
        return getFileInfos(getMediaFolder(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, false));
    }

    public static List<FileInfo> getVideoFolder(Context context) {
        return getFileInfos(getMediaFolder(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, false));
    }


    public static List<FileInfo> getDownloads(Context context) {
        return getFileInfos(Arrays.asList(DOWNLOAD_FOLDER.list()));
    }

    public static List<FileInfo> getDocument(Context context) {
        Cursor cursor = filterFile(context, FilterType.SUFFIX, new String[]{MediaStore.MediaColumns.DATA}, DOCUMENT);
        if (cursor == null) {
            return null;
        }
        Set<String> fileSet = new HashSet<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(0);
            fileSet.add(path);
        }
        //关闭Cursor
        cursor.close();
        return getFileInfos(fileSet);
    }

    private static List<FileInfo> getFileInfos(Collection<String> paths) {
        List<FileInfo> lstFile = new ArrayList<>();
        for (String path : paths) {
            File file = new File(path);
            FileInfo info = new FileInfo();
            info.setModify(file.lastModified() / 1000);
            info.setName(file.getName());
            info.setPath(file.getAbsolutePath());
            lstFile.add(info);
        }
        return lstFile;
    }

    private static Set<String> getMediaFolder(Context context, Uri uri, boolean onlyReturnParent) {
        Set<String> fileSet = new HashSet<>();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            if (onlyReturnParent) {
                int lastIndex = path.lastIndexOf("/");
                if (lastIndex == -1) {
                    Log.d(TAG, "getMediaFolder: " + path);
                    continue;
                }
                path = path.substring(0, lastIndex);
            }
            fileSet.add(path);
        }
        //关闭Cursor
        cursor.close();
        return fileSet;
    }

    public static Cursor filterFile(Context context, FilterType type, String[] projection, String filter) {
        return filterFile(context, type, projection, spiltFilter(filter));
    }

    public static Cursor filterFile(Context context, FilterType type, String[] projection, String[] filters) {
        if (FilterType.MIME_TYPE == type) {
            filters = getFiltersByMimeType(filters);
        } else {
            filters = getFiltersBySuffix(filters);
        }
        String selection = getSelection(type, filters);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, projection,
                selection, filters, MediaStore.MediaColumns.DATE_MODIFIED + " desc");
        return cursor;
    }

    private static String[] spiltFilter(String filter) {
        if (TextUtils.isEmpty(filter)) {
            return null;
        }
        return filter.split(SPLIT);
    }

    private static String[] getFiltersByMimeType(String[] filters) {
        if (filters == null) {
            return null;
        }
        //deal like image/* mime_type
        for (int i = 0; i < filters.length; i++) {
            if (filters[i].endsWith("*")) {
                filters[i] = filters[i].replace("*", "%");
            }
        }
        return filters;
    }

    /**
     * sample：select * from file where _data like '%.txt' or _data like '%.jpg'
     *
     * @return
     */
    private static String[] getFiltersBySuffix(String[] filters) {
        if (filters == null) {
            return null;
        }
        for (int i = 0; i < filters.length; i++) {
            boolean startWithDot = filters[i].trim().startsWith(".");
            filters[i] = "%" + (startWithDot ? "" : ".") + filters[i].trim();
        }
        return filters;
    }

    private static String getSelection(FilterType type, String[] filters) {
        if (filters == null || filters.length == 0) {
            return null;
        }
        String arg = FilterType.SUFFIX == type
                ? MediaStore.MediaColumns.DATA
                : MediaStore.MediaColumns.MIME_TYPE;
        StringBuilder selection = new StringBuilder();
        for (int i = 0; i < filters.length; i++) {
            selection.append(arg).append(" like ?");
            if (i < filters.length - 1) {
                selection.append(" or ");
            }
        }
        return selection.toString();
    }
}
