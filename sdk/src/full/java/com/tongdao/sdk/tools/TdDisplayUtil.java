package com.tongdao.sdk.tools;


import android.content.Context;
import android.graphics.drawable.Drawable;

public class TdDisplayUtil {

    public static int getRawPixel(Context ctx, float dp) {
        return (int) (ctx.getResources().getDisplayMetrics().density * dp + 0.5f); // +0.5f is used that the result is always at least 1px and not 0px
    }

    public static int getDIP(Context ctx, float px) {
        return (int) (px / ctx.getResources().getDisplayMetrics().density + 0.5f); // +0.5f is used that the result is always at least 1dp and not 0dp
    }
}
