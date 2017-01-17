package com.littlejie.core.net;

/**
 * Created by littlejie on 2017/1/10.
 */

public interface CustomRequestCallback extends RequestCallback {
    /**
     * 网络请求前需要做的工作，如显示加载效果
     */
    void before();

    /**
     * 网络请求后需要做的工作，如取消加载效果
     */
    void after();

}
