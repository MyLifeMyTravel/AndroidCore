package com.littlejie.core.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实体类与Json互转
 * Created by Lion on 2015/12/22.
 */
public class JsonUtil {

    private static Gson gson = new Gson();

    /**
     * 将 Map 转换成 Json
     *
     * @param map
     * @return
     */
    public static String map2Json(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            try {
                jsonObject.put(key, map.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    /**
     * Json 字符串转化成 map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2Map(String json) {
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, jsonObject.get(key));
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 实体对象转Json字符串
     *
     * @param o 实体对象
     * @return String
     */
    public static <T> String toJsonString(Object o) {
        return gson.toJson(o);
    }

    /**
     * 实体对象转JSONObject
     *
     * @param type
     * @param <T>
     * @return
     * @throws JSONException
     */
    public static <T> JSONObject toJson(T type) throws JSONException {
        return new JSONObject(toJsonString(type));
    }

    /**
     * Json字符串转实体对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }

    /**
     * 将数组转为相应对象的List
     *
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> klass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, new GsonList<T>(klass));
    }

    static class GsonList<T> implements ParameterizedType {

        private Class<?> wrapped;

        public GsonList(Class<T> wrapped) {
            this.wrapped = wrapped;
        }

        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }

    }
//另一种泛型解析array的方法
//    public static <T> List<T> fromJsonArray(String array, Class<T[]> clazz) {
//        final T[] lst = gson.fromJson(array, clazz);
//        return Arrays.asList(lst);
//    }

}