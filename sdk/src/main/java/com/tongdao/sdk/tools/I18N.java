package com.tongdao.sdk.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by kinjal.patel on 31/05/16.
 */
public class I18N  {
    public static final String DIALOG_TITLE = "Permission request";
    public static final String SETTINGS = "Settings";

//    final static ResourceBundle labels = ResourceBundle.getBundle("com.tongdao.sdk.ResourceBundle", Locale.ENGLISH);
//
//    public static String getValue(final String key)
//    {
//        return labels.getString(key);
//    }
//
//    public static Drawable getDrawableImage(final String imageName,
//                                            final Context context, final int width, final int height) {
//        Bitmap drawableBitmap = null;
//        Bitmap bitmapResized = null;
//        try {
//            final InputStream is = context.getClassLoader()
//                    .getResourceAsStream(imageName);
//            if (null != is) {
//                drawableBitmap = BitmapFactory.decodeStream(is);
//            }
//
//            if( width == 0 && height == 0 )
//                return new BitmapDrawable( drawableBitmap );
//            final int sizeX = width;
//            final int sizeY = height;
//
//            bitmapResized = Bitmap.createScaledBitmap(drawableBitmap, sizeX,
//                    sizeY, false);
//        } catch (final Exception e) {
//            // Log.e(StringConstants.SDK_NAME, StringConstants.ERR_MSG +
//            // e.getMessage());
//        }
//        return new BitmapDrawable(context.getResources(), bitmapResized);
//    }
}
