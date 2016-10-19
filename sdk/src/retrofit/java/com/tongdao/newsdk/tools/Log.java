package com.tongdao.newsdk.tools;

/**
 * Created by agonch on 10/10/16.
 */

public class Log {
    static boolean isDebug = true;

    public static void i(String tag, String message){
        if (isDebug)
            android.util.Log.i(tag, message);
    }

    public static void e(String tag, String message){
        if (isDebug)
            android.util.Log.e(tag, message);
    }

    public static void d(String tag, String message){
        if (isDebug)
            android.util.Log.d(tag, message);
    }

    public static void v(String tag, String message){
        if (isDebug)
            android.util.Log.v(tag, message);
    }
}
