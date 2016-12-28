package com.littlejie.core.util;

import android.net.Uri;

/**
 * Created by littlejie on 2016/12/14.
 */

public class MiscUtil {

    /**
     * 根据URL获取网站的icon
     *
     * @param url
     * @return
     */
    public static String getWebIcon(String url) {
        Uri uri = Uri.parse(url);
        return uri.getScheme() + "://" + uri.getHost() + "/favicon.ico";
    }
}
