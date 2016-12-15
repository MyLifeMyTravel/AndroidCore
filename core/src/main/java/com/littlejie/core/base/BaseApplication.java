package com.littlejie.core.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by littlejie on 2016/12/1.
 */

public class BaseApplication extends Application {

    /**
     * TAG
     */
    public static final String TAG = BaseApplication.class.getSimpleName();

    /**
     * the application instance
     */
    private static Context mInstance;

    /**
     * the list of opened activity
     */
    private static List<Activity> mLstActivities = new ArrayList<Activity>();

    /**
     * uiThreadHandler
     */
    private static Handler uiThreadHandler = null;

    /**
     * uiThread
     */
    private static Thread uiThread = null;

    public static Context getInstance() {
        return mInstance;
    }

    public static void runOnUIThread(Runnable work) {
        if (Thread.currentThread() != uiThread) {
            uiThreadHandler.post(work);
        } else {
            work.run();
        }
    }

    public static void runDelayOnUIThread(Runnable work, long time) {
        uiThreadHandler.postDelayed(work, time);
    }

    public static void removeRunnable(Runnable work) {
        uiThreadHandler.removeCallbacks(work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        uiThread = Thread.currentThread();
        uiThreadHandler = new Handler();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Core.init();
    }

    public static void addActivity(Activity activity) {
        mLstActivities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        mLstActivities.remove(activity);
    }

    public static void removeAllActivities() {
        mLstActivities.clear();
    }

    public static void finishAllActivities() {
        for (Activity activity : mLstActivities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        removeAllActivities();
    }

    public static void finishActivities(List<Class> lstClass) {
        Iterator it = mLstActivities.iterator();
        while (it.hasNext()) {
            Activity activity = (Activity) it.next();
            if (lstClass.contains(activity.getClass())) {
                activity.finish();
                it.remove();
            }
        }
    }

    public static boolean hasActivitiesExcept(List<Class> lstCls) {
        Iterator it = mLstActivities.iterator();
        while (it.hasNext()) {
            boolean exist = false;
            Activity activity = (Activity) it.next();
            for (Class cls : lstCls) {
                if (cls == activity.getClass()) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                continue;
            } else {
                return true;
            }
        }
        return false;
    }

    public static void finishActivitiesExcept(Class cls) {
        Iterator it = mLstActivities.iterator();
        while (it.hasNext()) {
            Activity activity = (Activity) it.next();
            if (activity.getClass() != cls) {
                activity.finish();
                it.remove();
            }
        }
    }

    public static boolean hasActivityVisible() {
        return mLstActivities.size() > 0;
    }

    public static void finishActivitiesExcept(Activity activity) {
        Iterator it = mLstActivities.iterator();
        while (it.hasNext()) {
            Activity a = (Activity) it.next();
            if (a != activity) {
                a.finish();
                it.remove();
            }
        }
    }

    public static Activity getActivity(Class cls) {
        for (Activity activity : mLstActivities) {
            if (activity.getClass() == cls) {
                return activity;
            }
        }
        return null;
    }

    public static Activity getLatestActivity() {
        if (mLstActivities.size() <= 0) {
            return null;
        }
        return mLstActivities.get(mLstActivities.size() - 1);
    }

    public static List<Activity> getRunActivities() {
        return mLstActivities;
    }

    @Override
    public void onTerminate() {
        removeAllActivities();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
