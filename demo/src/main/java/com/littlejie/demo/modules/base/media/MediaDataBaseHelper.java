package com.littlejie.demo.modules.base.media;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * Created by littlejie on 2017/2/4.
 */

public class MediaDataBaseHelper {

    //CONTENT_URI 具体使用见 MediaObserverActivity
    private static final Uri CONTENT_URI = MediaStore.Files.getContentUri("external");
    private static final String SPLIT = ";";

    public enum FilterType {
        SUFFIX, MIME_TYPE
    }

    /**
     * 文件过滤，如果 arg 为 mime_type，则按照文件类型过滤；否则按照文件后缀过滤
     *
     * @param context
     * @param type
     * @param filter
     * @return 返回Cursor对象，使用完毕请调用cursor.close()
     */
    public static Cursor filterFile(Context context, FilterType type, String[] projection, String filter) {
        return filterFile(context, type, projection, spiltFilter(filter));
    }

    public static Cursor filterFile(Context context, FilterType type, String[] projection, String[] filters) {
        //TODO 优化生成 filter 的方法
        if (FilterType.MIME_TYPE == type) {//根据文件的 MimeType 字段进行过滤
            filters = getFiltersByMimeType(filters);
        } else {//根据文件的 后缀 字段进行过滤
            filters = getFiltersBySuffix(filters);
        }
        //获取 selection 语句
        String selection = getSelection(type, filters);
        //进行数据库查询
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

    /**
     * 获取过滤条件
     * 示例：select * from files where mime_type like 'image/%' or mime_type like 'video/mp4'
     *
     * @return
     */
    private static String[] getFiltersByMimeType(String[] filters) {
        if (filters == null) {
            return null;
        }
        //形如 image/* 这类 mime_type 需要进行特殊处理
        for (int i = 0; i < filters.length; i++) {
            if (filters[i].endsWith("*")) {
                filters[i] = filters[i].replace("*", "%");
            }
        }
        return filters;
    }

    /**
     * 进行后缀过滤需要用到通配符，故此处做特殊处理
     * 示例：select * from file where _data like '%.txt' or _data like '%.jpg'
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

    /**
     * 生成 selection 语句
     *
     * @param type
     * @param filters
     * @return
     */
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
