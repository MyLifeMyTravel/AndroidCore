package com.littlejie.core.util;

import java.text.SimpleDateFormat;

/**
 * 时间相关工具类
 * Created by littlejie on 2017/1/6.
 */

public class TimeUtil {

    public static String parse2TimeDetail(long timeInMills) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(timeInMills);
        return dateString;
    }
}
