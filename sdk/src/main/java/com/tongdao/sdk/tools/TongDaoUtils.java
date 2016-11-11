/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongdao.sdk.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StatFs;
import android.support.v4.app.NotificationManagerCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;


public class TongDaoUtils {

    public static final String THUMBS_CACHE_DIR = "thumbs";
    public static final String IMAGES_CACHE_DIR = "images";
    public static final String HTTP_CACHE_DIR = "http";
    private static Context cxt;

    public TongDaoUtils(Context context) {
        cxt = context;
    }

    /**
     * Download a bitmap from a URL and write the content to an output stream.
     *
     * @param urlString The URL to fetch
     * @return true if successful, false otherwise
     */
    public static boolean downloadUrlToStream(String urlString,
                                              OutputStream outputStream) {
        disableConnectionReuseIfNecessary();
        HttpsURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpsURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            out = new BufferedOutputStream(outputStream);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e("IOException", "Error in downloadBitmap");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (final IOException e) {
                out = null;
                in = null;
            }
        }
        return false;
    }

    /**
     * copy a file from a path and write the content to an output stream.
     *
     * @return true if successful, false otherwise
     */
    public static boolean copyFileToStream(String pathString,
                                           OutputStream outputStream) {
        if (pathString == null) {
            return false;
        }
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            File file = new File(pathString);
            if (!file.exists()) {
                return false;
            }
            in = new BufferedInputStream(new FileInputStream(file));
            out = new BufferedOutputStream(outputStream);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            return true;
        } catch (IOException e) {
            Log.e("IOException", "Error in copyFileToStream");
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (final IOException e) {
                out = null;
                in = null;
            }
        }
        return false;
    }


    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        return new File(getDiskCacheDirPath(context, uniqueName));
    }

    public static String getDiskCacheDirPath(Context context, String uniqueName) {
        // maybe later will use external
        return context.getCacheDir().getPath() + File.separator + uniqueName;
    }

    /**
     * A hashing method that changes a string (like a URL) into a hash suitable
     * for using as a disk filename.
     */
    static MessageDigest mDigest;

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            if (mDigest == null) {
                mDigest = MessageDigest.getInstance("MD5");
            }
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap
     * @return size in bytes
     */
    @TargetApi(12)
    public static int getBitmapSize(Bitmap bitmap) {
        if (hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Method to check whether notifications are enabled on the device. Currently I am trying the
     * new NotificationManagerCompat feature introduced in Android 24.
     * @return
     */
    public boolean isNotificationEnabled() {
        return NotificationManagerCompat.from(cxt).areNotificationsEnabled();
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @SuppressWarnings("deprecation")
    @TargetApi(9)
    public static long getUsableSpace(File path) {
        if (hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
}
