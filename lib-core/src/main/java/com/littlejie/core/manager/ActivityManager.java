package com.littlejie.core.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by littlejie on 2017/1/6.
 */

public class ActivityManager {

    /**
     * the list of opened activity
     */
    private static List<Activity> mLstActivities = new ArrayList<Activity>();

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

    public static boolean hasActivity(Class clazz) {
        Iterator<Activity> iterator = mLstActivities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (clazz == activity.getClass()) {
                return true;
            }
        }
        return false;
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

    public static void startActivity(Context context, Class clz) {
        context.startActivity(new Intent(context, clz));
    }
}
