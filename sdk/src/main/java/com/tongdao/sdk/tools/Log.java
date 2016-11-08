package com.tongdao.sdk.tools;

import com.tongdao.sdk.BuildConfig;

/**
 * Created by agonch on 10/10/16.
 */

public class Log {
    static boolean isDebug = BuildConfig.DEBUG;
    static String cache = "";

    public static void i(String tag, String message){
        if (isDebug){
            android.util.Log.i(tag, message);
            cache += "\n " + tag + ", " + message;
        }

    }

    public static void e(String tag, String message){
        if (isDebug){
            android.util.Log.e(tag, message);
            cache += "\n " + tag + ", " + message;
        }

    }

    public static void d(String tag, String message){
        if (isDebug){
            android.util.Log.d(tag, message);
            cache += "\n " + tag + ", " + message;
        }

    }

    public static void v(String tag, String message){
        if (isDebug){
            android.util.Log.v(tag, message);
            cache += "\n " + tag + ", " + message;
        }

    }

    public static String readCache(){
        String result = cache;
        cache = "";
        return result;
    }
}
