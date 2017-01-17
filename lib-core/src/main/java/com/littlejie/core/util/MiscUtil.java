package com.littlejie.core.util;

import android.net.Uri;

import java.security.MessageDigest;

/**
 * 不知怎么分类的工具方法
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

    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String content) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = content.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }
}
