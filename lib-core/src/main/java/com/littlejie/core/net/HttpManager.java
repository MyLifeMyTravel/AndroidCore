package com.littlejie.core.net;

import com.littlejie.core.base.Core;
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
    private static OkHttpClient mOkHttpClient;

    private HttpManager() {
    }

    /**
     * 初始化，最好是放在 Application.onCreate() 方法中执行
     * 一次初始化，且不会造成内存泄漏
     */
    public static void init() {
        mOkHttpClient = new OkHttpClient();
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
