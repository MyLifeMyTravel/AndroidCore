package com.littlejie.core.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference 工具类
 * Created by littlejie on 2017/2/13.
 */

public final class SharePrefsUtil {

    /**
     * TAG
     */
    private static final String TAG = "SharedPrefs";

    /**
     * instance
     */
    private static SharePrefsUtil mInstance;

    /**
     * context
     */
    private Context context = null;

    /**
     * mSP
     */
    private static SharedPreferences mSP = null;

    /**
     *
     */
    private SharePrefsUtil() {

    }

    public void init(Context context, String fileName) {
        mSP = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * @return
     */
    public static synchronized SharePrefsUtil getInstance() {
        if (mInstance == null) {
            mInstance = new SharePrefsUtil();
        }
        return mInstance;
    }

    /**
     * @param field
     * @return
     */
    public boolean getBoolean(String field, boolean defaultValue) {
        return mSP.getBoolean(field, defaultValue);
    }

    /**
     * @param field
     * @return
     */
    public int getInt(String field, int defaultValue) {
        return mSP.getInt(field, defaultValue);
    }

    /**
     * @param field
     * @return
     */
    public float getFloat(String field, float defaultValue) {
        return mSP.getFloat(field, defaultValue);
    }

    /**
     * @param field
     * @return
     */
    public long getLong(String field, long defaultValue) {
        return mSP.getLong(field, defaultValue);
    }

    /**
     * @param field
     * @return
     */
    public String getString(String field, String defaultValue) {
        return mSP.getString(field, defaultValue);
    }

    /**
     * @param field
     * @param value
     */
    public void setBoolean(String field, boolean value) {
        mSP.edit().putBoolean(field, value).commit();
    }

    /**
     * @param field
     * @param value
     */
    public void setInt(String field, int value) {
        mSP.edit().putInt(field, value).commit();
    }

    /**
     * @param field
     * @param value
     */
    public void setFloat(String field, float value) {
        mSP.edit().putFloat(field, value).commit();
    }

    /**
     * @param field
     * @param value
     */
    public void setLong(String field, long value) {
        mSP.edit().putLong(field, value).commit();
    }

    /**
     * @param field
     * @param value
     */
    public void setString(String field, String value) {
        mSP.edit().putString(field, value).commit();
    }

    public void remove(String field) {
        mSP.edit().remove(field).commit();
    }

    public boolean contains(String field) {
        return mSP.contains(field);
    }

    public void clear() {
        mSP.edit().clear().commit();
    }
}
