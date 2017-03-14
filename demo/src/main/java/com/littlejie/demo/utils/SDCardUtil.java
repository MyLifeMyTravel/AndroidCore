package com.littlejie.demo.utils;

import android.content.Context;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import android.util.Log;

import com.littlejie.core.base.Core;
import com.littlejie.core.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * SD卡 工具类
 * Created by littlejie on 2017/3/13.
 */

public class SDCardUtil {

    private static final String TAG = SDCardUtil.class.getSimpleName();

    /**
     * 判断是否安装有 SD卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        return getRemovableStorageDir() != null;
    }

    /**
     * 根据路径来判断是否为 SD卡 路径
     *
     * @param path
     * @return
     */
    public static boolean isSDCardPath(String path) {
        if (TextUtils.isEmpty(path)
                || !hasSDCard()) {
            return false;
        }
        String sdcard = getRemovableStorageDir().getAbsolutePath();
        return path.contains(sdcard);
    }

    /**
     * 获取 SD卡 访问权限
     *
     * @param context
     * @return
     */
    public static UriPermission getSDCardAccessPermission(Context context) {
        if (!hasSDCard()) {
            return null;
        }
        List<UriPermission> lstPermissions = context.getContentResolver().getPersistedUriPermissions();
        UriPermission sdcardAccessPermission = null;
        for (UriPermission permission : lstPermissions) {
            if (permission.getUri().toString().endsWith("%3A")) {
                sdcardAccessPermission = permission;
                break;
            }
        }
        return sdcardAccessPermission;
    }

    public static DocumentFile getDocumentFile(Context context, String path, boolean isDir) {
        if (!hasSDCard()) {
            return null;
        }
        UriPermission permission = getSDCardAccessPermission(context);
        if (permission == null) {
            return null;
        }

        DocumentFile document = mkdirs(DocumentFile.fromTreeUri(context, permission.getUri()), path, isDir);
        return isDir ? document : document.createFile(getMimeType(path), getDisplayName(path));
    }

    private static DocumentFile mkdirs(DocumentFile parent, String path, boolean isDir) {
        String[] paths = getPaths(path, isDir);
        if (paths == null || paths.length == 0) {
            return parent;
        }
        for (String name : paths) {
            if (parent.findFile(name) == null) {
                parent = parent.createDirectory(name);
            } else {
                parent = parent.findFile(name);
            }
        }
        return parent;
    }

    private static String[] getPaths(String path, boolean isDir) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("文件路径不能为空");
        }
        String root = getRemovableStorageDir().getAbsolutePath();
        if (path.equals(root)) {
            return null;
        }
        path = path.substring(root.length() + (root.endsWith("/") ? 0 : 1));
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        if (isDir) {
            return path.split("/");
        } else {
            int lastIndex = path.lastIndexOf("/");
            if (lastIndex != -1) {
                return path.substring(0, lastIndex).split("/");
            }
            return null;
        }
    }

    /**
     * 根据路径获取文件名
     *
     * @param path
     * @return
     */
    private static String getDisplayName(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        return file.getName();
    }

    /**
     * 根据文件后缀获取 mimeType
     *
     * @param path
     * @return
     */
    private static String getMimeType(String path) {
        return FileUtil.getFileMimeType(path);
    }

    public static void write2SDCard(Context context, String path, boolean isDir, InputStream is) throws IOException {
        write2SDCard(context, getDocumentFile(context, path, isDir), is);
    }

    public static void write2SDCard(Context context, Uri treeUri, InputStream is) throws IOException {
        write2SDCard(context, DocumentFile.fromTreeUri(context, treeUri), is);
    }

    public static void write2SDCard(Context context, DocumentFile document, InputStream is) throws IOException {
        if (document.isDirectory()) {
            throw new RuntimeException("this document is directory");
        }
        OutputStream out = context.getContentResolver().openOutputStream(document.getUri());
        if (out == null) {
            throw new IOException("open document failed.");
        }
        try {
            // Create a new file and write into it
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } finally {
            if (is != null) {
                is.close();
            }
            out.close();
        }
    }

    public static void copy(Context context, String src, String desc) throws IOException {
        write2SDCard(context, desc, new File(src).isDirectory(), new BufferedInputStream(new FileInputStream(src)));
    }

    public static boolean move(Context context, String src, String dest) throws IOException {
        if (isSDCardPath(src)) {
            DocumentFile srcFile = getDocumentFile(context, src, true);
            DocumentFile destFile = getDocumentFile(context, dest, new File(src).isDirectory());
            write2SDCard(context, destFile, context.getContentResolver().openInputStream(srcFile.getUri()));
            return srcFile.delete();
        } else {
            File srcFile = new File(src);
            DocumentFile destFile = getDocumentFile(context, dest, new File(src).isDirectory());
            write2SDCard(context, destFile, new BufferedInputStream(new FileInputStream(srcFile)));
            return srcFile.delete();
        }
    }

    public static boolean rename(Context context, String src, String dest) throws FileNotFoundException {
        DocumentFile srcFile = getDocumentFile(context, src, true);
        if (srcFile == null) {
            throw new FileNotFoundException("this document doesn't exists.");
        }
        String display = getDisplayName(dest);
        return srcFile.renameTo(display);
    }

    public static boolean delete(Context context, String path, boolean isDir) throws FileNotFoundException {
        return delete(getDocumentFile(context, path, isDir));
    }

    public static boolean delete(DocumentFile documentFile) throws FileNotFoundException {
        if (!documentFile.exists()) {
            throw new FileNotFoundException("Document not found");
        }
        return documentFile.delete();
    }

    public static OutputStream getOutputStream(Context context, String path) throws IOException {
        DocumentFile file = getDocumentFile(context, path, new File(path).isDirectory());
        if (file == null) {
            throw new FileNotFoundException("this document doesn't exists.");
        }
        return context.getContentResolver().openOutputStream(file.getUri());
    }

    /**
     * if (Environment.isExternalStorageEulated()) {
     * // The primary and internal storage is FUSE and there is no phone storage
     * String fusePath = Environment.getExternalStorageDirectory().toString();
     * if (Environment.hasRemovableStorageSlot()) {
     * // The project also has SD card as the secondary storage
     * String sdPath = Environment.getRemovableStorageDirectory().toString();
     * }
     * } else if (Environment.isExternalStorageRemovable()) {
     * // Non FUSE and the primary storage is SD card
     * String sdPath = Environment.getExternalStorageDirectory().toString();
     * if (Environment.hasPhoneStorage()) {
     * // The project also has phone storage as the secondary storage
     * String phonePath = Environment.getPhoneStorageDirectory().toString();
     * }
     * } else {
     * // The primary storage is phone storage
     * String phonePath = Environment.getExternalStorageDirectory().toString();
     * if (Environment.hasRemovableStorageSlot()) {
     * // The project also has SD card as the secondary storage
     * String sdPath = Environment.getRemovableStorageDirectory().toString();
     * }
     * }
     */
    public static File getRemovableStorageDir() {
        ///storage/emulated/0/Android/data/com.example.fashionshows/files
        ///storage/7CB9-0DEA/Android/data/com.example.fashionshows/files

        Log.d(TAG, "getRemovableStorageDir, start.");

        if (Core.getApplicationContext() == null) {
            Log.d(TAG, "getRemovableStorageDir, context is null.");
            return null;
        }

        try {
            File mountPoints[] = Core.getApplicationContext()
                    .getExternalFilesDirs(null);

            if (mountPoints != null && mountPoints.length > 0) {
                String phoneStorageMountPoint = Environment
                        .getExternalStorageDirectory().toString();

                for (File file : mountPoints) {

                    if (file == null) {
                        Log.d(TAG, "getRemovableStorageDir, file path is null.");
                        continue;
                    }

                    String path = file.getPath();
                    int endIndex = path.indexOf("/Android");
                    String sdCardmountPoint = path.substring(0, endIndex);

                    Log.d(TAG, "getRemovableStorageDir, "
                            + sdCardmountPoint);

                    if (!sdCardmountPoint
                            .equalsIgnoreCase(phoneStorageMountPoint)) {
                        return new File(sdCardmountPoint);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getRemovableStorageDir, exception happened:" + e.getMessage());
        }

        Log.d(TAG, "getRemovableStorageDir, end.");

        return null;
    }

}
