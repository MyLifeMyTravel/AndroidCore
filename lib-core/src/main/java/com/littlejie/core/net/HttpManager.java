package com.littlejie.core.net;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.littlejie.core.base.Core;
import com.littlejie.core.util.FileUtil;
import com.littlejie.core.util.JsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http请求封装，基于OKHTTP
 * Created by littlejie on 2016/12/22.
 */

//Todo 添加一个接口，用于执行HTTP请求前和请求后的处理
public class HttpManager {

    private static final String TAG = HttpManager.class.getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static DownloadManager mDownloadManager;
    private static OkHttpClient mOkHttpClient;

    public interface OnDownloadCompleteListener {
        void onDownloadComplete(long id, boolean success);
    }

    public interface OnDownloadStatusListener {
        void onDownloadStatus(String uri, int status, long download, long total);
    }

    private HttpManager() {
    }

    /**
     * 初始化，最好是放在 Application.onCreate() 方法中执行
     * 一次初始化，且不会造成内存泄漏
     */
    public static void init() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) Core.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
    }

    public static long downloadBySystem(String url, String subPath, OnDownloadCompleteListener listener) {
        return downloadBySystem(url, Environment.DIRECTORY_DOWNLOADS, subPath,
                DownloadManager.Request.NETWORK_WIFI, DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION,
                null, listener);
    }

    public static long downloadBySystem(String url, String subPath, int notificationVisibility,
                                        Map<String, String> headers, OnDownloadCompleteListener listener) {
        return downloadBySystem(url, Environment.DIRECTORY_DOWNLOADS, subPath,
                DownloadManager.Request.NETWORK_WIFI, notificationVisibility, headers, listener);
    }

    /**
     * 调用系统DownloadManager下载
     *
     * @param url
     * @param dirPath      参考setDestinationInExternalPublicDir的dirPath
     * @param subPath      参考setDestinationInExternalPublicDir的subPath
     * @param networkTypes
     * @param headers
     * @param listener
     * @return
     */
    public static long downloadBySystem(String url, String dirPath, String subPath, int networkTypes, int notificationVisibility,
                                        Map<String, String> headers, final OnDownloadCompleteListener listener) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        if (headers != null) {
            Set<String> keySets = headers.keySet();
            for (String key : keySets) {
                request.addRequestHeader(key, headers.get(key));
            }
        }

        //在什么网络下进行下载
        request.setAllowedNetworkTypes(networkTypes);
        //获取文件的MimeType
        String mimeType = FileUtil.getMimeType(url);
        request.setMimeType(mimeType);

        //下载时是否在通知栏显示
        request.setNotificationVisibility(notificationVisibility);

        //设置下载位置
        request.setDestinationInExternalPublicDir(dirPath, subPath);

        final long taskID = mDownloadManager.enqueue(request);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == taskID) {
                    Core.getApplicationContext().unregisterReceiver(this);
                    if (listener != null) {
                        listener.onDownloadComplete(downloadID, true);
                    }
                }
            }
        };
        Core.getApplicationContext().registerReceiver(receiver, filter);
        return taskID;
    }

    /**
     * 根据downloadID查询对应下载任务的信息
     *
     * @param downloadID
     * @param listener
     */
    public static void querySystemDownloadStatus(long downloadID, OnDownloadStatusListener listener) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }
        //该下载任务当前状态
        int status = cursor.getInt(cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)));
        //下载文件在本地的uri
        String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        // 下载的文件总大小
        int total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        // 截止目前已经下载的文件总大小
        int download = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        cursor.close();
        if (listener != null) {
            listener.onDownloadStatus(uri, status, download, total);
        }
    }

    /**
     * 同步get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * 同步get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        //创建请求Request
        final Request request = buildRequest(url, headers);
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 异步get请求
     *
     * @param url
     * @param callback
     */
    public static Call getAsync(String url, RequestCallback callback) {
        return asyncRequest(url, null, callback);
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param body     post内容
     * @param callback
     */
    public static Call postAsync(String url, String body, RequestCallback callback) {
        RequestBody requestBody = RequestBody.create(null, body);
        return asyncRequest(buildRequest(url, requestBody), callback);
    }

    /**
     * 异步post请求，数据格式为json
     *
     * @param url
     * @param body
     * @param callback
     */
    public static Call postJsonAsync(String url, Object body, RequestCallback callback) {
        return postJsonAsync(url, JsonUtil.toJsonString(body), callback);
    }

    public static Call postJsonAsync(String url, String body, RequestCallback callback) {
        return postJsonAsync(url, body, null, callback);
    }

    public static Call postJsonAsync(String url, Object body, Map<String, String> headers, RequestCallback callback) {
        return postJsonAsync(url, JsonUtil.toJsonString(body), headers, callback);
    }

    public static Call postJsonAsync(String url, String body, Map<String, String> headers, RequestCallback callback) {
        RequestBody requestBody = RequestBody.create(JSON, body);
        return asyncRequest(buildRequest(url, requestBody, headers), callback);
    }

    /**
     * 异步上传文件
     *
     * @param url
     * @param file
     * @param headers
     * @param callback
     */
    public static Call postFileAsync(String url, File file, Map<String, String> headers, RequestCallback callback) {
        //获取文件的mimeType类型
        MediaType type = MediaType.parse(FileUtil.getFileMimeType(file.getAbsolutePath()));
        RequestBody requestBody = RequestBody.create(type, file);
        return asyncRequest(buildRequest(url, requestBody, headers), callback);
    }

    public static Call downloadFileAsync(String url, final String file, OnDownloadCompleteListener listener) {
        return downloadFileAsync(url, new File(file), listener);
    }

    public static Call downloadFileAsync(String url, final File file, final OnDownloadCompleteListener listener) {
        Call call = mOkHttpClient.newCall(buildRequest(url));
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyDownloadComplete(false, listener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    notifyDownloadComplete(true, listener);
                } catch (IOException e) {
                    e.printStackTrace();
                    notifyDownloadComplete(false, listener);
                } finally {
                    try {
                        is.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return call;
    }

    private static void notifyDownloadComplete(final boolean success, final OnDownloadCompleteListener listener) {
        if (listener == null) {
            return;
        }
        Core.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                listener.onDownloadComplete(0, success);
            }
        });
    }

    public static Call asyncRequest(String url, Map<String, String> headers, RequestCallback callback) {
        return asyncRequest(buildRequest(url, headers), callback);
    }

    private static Call asyncRequest(Request request, final RequestCallback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                Core.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call.request(), e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                //此处必须放在 runOnUIThread() 方法外，不然 string() 会报 java.lang.IllegalStateException: closed
                //具体原因未知
                //对于 Handler 更新 UI 同样适用
                //response.body().string()方法只能调用一次
                final String body = response.body().string();
                Core.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response, body);
                    }
                });
            }
        });
        return call;
    }

    private static Request buildRequest(String url) {
        return buildRequest(url, null, null);
    }

    private static Request buildRequest(String url, RequestBody body) {
        return buildRequest(url, body, null);
    }

    private static Request buildRequest(String url, Map<String, String> headers) {
        return buildRequest(url, null, headers);
    }

    private static Request buildRequest(String url, RequestBody body, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (body != null) {
            builder.post(body);
        }
        if (headers != null) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                builder.addHeader(key, headers.get(key));
            }
        }
        return builder.build();
    }
}
