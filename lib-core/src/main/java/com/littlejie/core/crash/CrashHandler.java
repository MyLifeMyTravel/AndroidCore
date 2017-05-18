package com.littlejie.core.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.littlejie.core.base.Core;
import com.littlejie.core.util.FileUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class CrashHandler implements UncaughtExceptionHandler {

    /**
     * LANGUAGE
     */
    private static final String TAG = "CrashHandler";

    /**
     * mDefaultHandler
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * sInstance
     */
    private static CrashHandler sInstance = new CrashHandler();

    /**
     * mInfos
     */
    private Map<String, String> mInfos = new HashMap<String, String>();

    /**
     * mFormatter
     */
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String mPath;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            sInstance = new CrashHandler();
        }
        return sInstance;
    }

    public void init(String crashFolder) {
        this.mPath = crashFolder;

        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectDeviceInfo(Core.getApplicationContext());
        writeCrashInfoToFile(ex);
        return true;
    }

    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfos.put("versionName", versionName);
                mInfos.put("versionCode", versionCode);
                mInfos.put("crashTime", mFormatter.format(new Date()));
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    private void writeCrashInfoToFile(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        sb.append("\n\n");

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            // TODO write crash log to file
            boolean suffix = mPath.endsWith("/");
            FileUtil.write(mPath + (suffix ? "" : "/") + mFormatter.format(new Date()), sb.toString());
        } catch (Exception e) {
            Log.e(TAG, "an error occur while writing file...", e);
        }
    }

}
