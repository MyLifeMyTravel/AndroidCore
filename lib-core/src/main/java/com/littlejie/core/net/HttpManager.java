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

import java.io.IOException;
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
 * Created by littlejie on 2016/12/22.
 */

//Todo 添加一个接口，用于执行HTTP请求前和请求后的处理
public class HttpManager {

    private static final String TAG = HttpManager.class.getSimpleName();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static DownloadManager mDownloadManager;
    private static OkHttpClient mOkHttpClient;

    public interface OnDownloadCompleteListener {
        void onDownloadComplete();
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
        return downloadBySystem(url, Environment.DIRECTORY_DOWNLOADS, subPath, DownloadManager.Request.NETWORK_WIFI, null, listener);
    }

    public static long downloadBySystem(String url, String subPath, Map<String, String> headers, OnDownloadCompleteListener listener) {
        return downloadBySystem(url, Environment.DIRECTORY_DOWNLOADS, subPath, DownloadManager.Request.NETWORK_WIFI, headers, listener);
    }

    /**
     *
     * @param url
     * @param dirPath 参考setDestinationInExternalPublicDir的dirPath
     * @param subPath 参考setDestinationInExternalPublicDir的subPath
     * @param networkTypes
     * @param headers
     * @param listener
     * @return
     */
    public static long downloadBySystem(String url, String dirPath, String subPath, int networkTypes,
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
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);

        //设置下载位置
        request.setDestinationInExternalPublicDir(dirPath, subPath);

        final long id = mDownloadManager.enqueue(request);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {
                    Core.getApplicationContext().unregisterReceiver(this);
                    if (listener != null) {
                        listener.onDownloadComplete();
                    }
                }
            }
        };
        Core.getApplicationContext().registerReceiver(receiver, filter);
        return id;
    }

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

    public static void getAsync(String url, RequestCallback callback) {
        asyncRequest(url, null, callback);
    }

    public static void postAsync(String url, String name, RequestCallback callback) {
        RequestBody body = RequestBody.create(null, name);
        asyncRequest(buildRequest(url, body), callback);
    }

    public static void postJsonAsync(String url, Object body, RequestCallback callback) {
        postJsonAsync(url, JsonUtil.toJsonString(body), callback);
    }

    public static void postJsonAsync(String url, String body, RequestCallback callback) {
        postJsonAsync(url, body, null, callback);
    }

    public static void postJsonAsync(String url, Object body, Map<String, String> headers, RequestCallback callback) {
        postJsonAsync(url, JsonUtil.toJsonString(body), headers, callback);
    }

    public static void postJsonAsync(String url, String body, Map<String, String> headers, RequestCallback callback) {
        RequestBody requestBody = RequestBody.create(JSON, body);
        asyncRequest(buildRequest(url, requestBody, headers), callback);
    }

    public static void asyncRequest(String url, Map<String, String> headers, RequestCallback callback) {
        asyncRequest(buildRequest(url, headers), callback);
    }

    private static void asyncRequest(Request request, final RequestCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
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
                final String body = response.body().string();
                Core.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response, body);
                    }
                });
            }
        });
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
