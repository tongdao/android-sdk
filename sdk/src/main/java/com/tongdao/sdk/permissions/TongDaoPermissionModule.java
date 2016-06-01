package com.tongdao.sdk.permissions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by kinjal.patel on 27/04/16.
 */
public class TongDaoPermissionModule {

    private static IPermissionResponse iPermissionResponse;

    public static IPermissionResponse getPermissionResponse() {
        return iPermissionResponse;
    }

    public static boolean checkPermission(Context context, final String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        Activity activity = (Activity)context;
        if (PackageManager.PERMISSION_GRANTED == activity.checkSelfPermission(permission)) {
            return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    public static void requestPermission(final Context context, final int requestCode,
                                         final String permission, final IPermissionResponse callback) {
        iPermissionResponse = callback;

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("permission", permission);
        intent.putExtra("requestCode", requestCode);
        context.startActivity(intent);
    }
}
