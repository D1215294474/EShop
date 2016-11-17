package com.feicuiedu.eshop.base.utils;

import android.util.Log;

/**
 * 日志工具类.
 */
public class LogUtils {

    private LogUtils() {
        throw new UnsupportedOperationException("LogUtils can't be instantiated.");
    }

    private static boolean isDebug = true;
    private static final String TAG = "EShop";

    public static void verbose(String msg) {
        if (isDebug) Log.v(TAG, msg);
    }

    public static void info(String msg) {
        if (isDebug) Log.i(TAG, msg);
    }

    public static void debug(String msg) {
        if (isDebug) Log.d(TAG, msg);
    }

    public static void error(String msg) {
        if (isDebug) Log.e(TAG, msg);
    }


}
