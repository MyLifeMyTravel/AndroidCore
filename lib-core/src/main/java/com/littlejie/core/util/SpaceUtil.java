package com.littlejie.core.util;

import java.text.DecimalFormat;

import static com.littlejie.core.util.SpaceUtil.Unit.MB;

/**
 * 存储空间格式化
 * Created by littlejie on 2017/2/10.
 */

public class SpaceUtil {

    //默认保留两位小数
    public static final int DEFAULT_DECIMAL = 2;
    //默认存储单位为MB
    public static final Unit DEFAULT_UNIT = MB;

    enum Unit {
        B, KB, MB, GB, TB
    }

    public static String getSpaceWithUnit(long space) {
        int i = 0;
        long tmp = space;
        for (; ; i++) {
            if ((space / 1024) <= 0) {
                break;
            }
            space = space / 1024;
        }

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(tmp * 1.0f / Math.pow(1024, i)) + " " + Unit.values()[i];
    }

    public static String getSpaceWithUnit(long space, int decimal) {
        return getSpaceWithUnit(space, DEFAULT_UNIT, decimal);
    }

    /**
     * 根据传入的space大小，转换为对应单位的存储空间字符串
     * <p>
     * 如：getSpaceWithUnit(1024,KB,0)，则转换后获取到的值为1KB
     *
     * @param space   存储空间大小，默认为 B
     * @param unit    要转换成的存储空间的单位
     * @param decimal 转换后的 String 保留几位小数
     * @return
     */
    public static String getSpaceWithUnit(long space, Unit unit, int decimal) {
        long i = 0;
        switch (unit) {
            case B:
                i = space;
                break;
            case KB:
                i = space >> 10;
                break;
            case MB:
                i = space >> 20;
                break;
            case GB:
                i = space >> 30;
                break;
            case TB:
                i = space >> 40;
                break;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(i * 1.0f);
    }

}
