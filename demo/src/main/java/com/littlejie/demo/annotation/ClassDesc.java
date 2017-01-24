package com.littlejie.demo.annotation;

import android.content.Context;

import java.util.List;

/**
 * 库主入口，获取使用注解的类及其描述信息，写入数据库，方便查询
 * <p>
 * Created by littlejie on 2017/1/18.
 */

public class ClassDesc {

    public static void init(Context context) {
        List<Class> clazzList = ClassUtil.getAllClassByAnnotation(Description.class);
    }
}
