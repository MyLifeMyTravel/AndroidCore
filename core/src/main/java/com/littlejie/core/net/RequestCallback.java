package com.littlejie.core.net;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by littlejie on 2016/12/23.
 */

public interface RequestCallback {

    void before();

    /**
     * HTTP请求成功
     * 由于在主线程中调用 response.body().string() 会抛出 NetworkOnMainThreadException
     * 故，直接在子线程中直接取出 body 回调
     * 补充：此处未考虑 body 中的数据过大的情况
     *
     * @param response
     * @param body
     */
    void onResponse(Response response, String body);

    /**
     * HTTP请求失败，根据 Request 对象可以进行重试
     *
     * @param request
     * @param e
     */
    void onFailure(Request request, IOException e);

    void after();

    class SimpleRequestCallback implements RequestCallback {

        @Override
        public void before() {

        }

        @Override
        public void onResponse(Response response, String body) {

        }

        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void after() {

        }
    }
}
