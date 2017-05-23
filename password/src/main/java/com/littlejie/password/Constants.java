package com.littlejie.password;

/**
 * Copyright (c) 2017, Bongmi
 * All rights reserved
 * Author: littlejie@bongmi.com
 */

public class Constants {

    //键盘上的数字，-1 代表左下角按钮，-2代表返回按钮
    public static final int[] KEYBOARD_VALUE = {1, 2, 3, 4, 5, 6, 7, 8, 9, -1, 0, -2};

    public static final String PARAMS_SET_PASSWORD_TYPE = "set_password";
    public static final String PARAMS_LOCK_MILLS = "lock_mills";

    public static final int TYPE_SET_PASSWORD = 0;
    public static final int TYPE_REENTER_PASSWORD = 1;
    public static final int TYPE_CLOSE_PASSWORD = 2;
    public static final int TYPE_MODIFY_PASSWORD = 3;
}
