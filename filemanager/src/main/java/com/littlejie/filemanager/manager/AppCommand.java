package com.littlejie.filemanager.manager;

import com.littlejie.filemanager.impl.OnBackPressedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by littlejie on 2017/2/12.
 */

public class AppCommand {

    //可以考虑用Set，自动过滤重复监听器
    private static List<OnBackPressedListener> sOnBackPressedListeners
            = new ArrayList<>();

    public static void addOnBackPressedListener(OnBackPressedListener listener) {
        if (!sOnBackPressedListeners.contains(listener)) {
            sOnBackPressedListeners.add(listener);
        }
    }

    public static void removeOnBackPressedListener(OnBackPressedListener listener) {
        sOnBackPressedListeners.remove(listener);
    }

    /**
     * 如果返回true，则代表该返回按钮事件被消费
     *
     * @return
     */
    public static boolean onBackPressed() {
        for (OnBackPressedListener listener : sOnBackPressedListeners) {
            if (listener.onBackPressed()) {
                return true;
            }
        }
        return false;
    }

}
