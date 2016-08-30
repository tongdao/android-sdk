package com.tongdao.sdk.tools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;

public class TongDaoAppInfoTool {
    private static final String CONNECTION_TYPE_UNKNOWN = "UNKNOWN";
    private static final String CONNECTION_TYPE_WIFI = "WIFI";
    private static final String CONNECTION_TYPE_WIMAX = "WIMAX";
    private static final String CONNECTION_TYPE_MOBILE_UNKNOWN = "MOBILE";
    private static final String CONNECTION_TYPE_MOBILE_1xRTT = "1xRTT";
    private static final String CONNECTION_TYPE_MOBILE_CDMA = "CDMA";
    private static final String CONNECTION_TYPE_MOBILE_EDGE = "EDGE";
    private static final String CONNECTION_TYPE_MOBILE_EVDO_0 = "EVDO_0";
    private static final String CONNECTION_TYPE_MOBILE_EVDO_A = "EVDO_A";
    private static final String CONNECTION_TYPE_MOBILE_GPRS = "GPRS";
    private static final String CONNECTION_TYPE_MOBILE_UMTS = "UMTS";
    public static final String ACCESS_COARSE_LOCATION_PERMISSION = "android.permission.ACCESS_COARSE_LOCATION";
    public static final String ACCESS_FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_TELEPHONY_PERMISSION = "android.permission.READ_PHONE_STATE";
    private static final String OS_NAME = "android";
    private static final String UNKNOWN = "unknown";
    private static final String PHONE_SERVICE = "phone";
    private static final String FINE = "fine";
    private static final String COARSE = "coarse";

    public static final Semaphore LOCK = new Semaphore(0);

    public static Object[] getDeviceInfo(Context c) {
        String model = Build.MODEL;
        String brand = Build.MANUFACTURER;
        String product_id = Build.PRODUCT;
        String build_id = Build.DISPLAY;
        String device_name = Build.DEVICE;
        String os_version = VERSION.RELEASE;
        String language = Locale.getDefault().toString();
        //String serial_number = DeviceUuidFactory.getDeviceUuid(c).toString();
        //TelephonyManager tm = (TelephonyManager) c.getSystemService(PHONE_SERVICE);
        //String imei = tm.getDeviceId();
        return new Object[]{model, brand, product_id, build_id, device_name,
                OS_NAME, os_version, language};
    }

    public static Object[] getVersionCodeOsName(Context appContext) {
        try {
            PackageInfo pi = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
            int versionCode = pi.versionCode;
            String versionName = pi.versionName;
            int os_version = VERSION.SDK_INT;
            String language = Locale.getDefault().toString();
            return new Object[]{versionCode, versionName, OS_NAME, os_version, language};
        } catch (NameNotFoundException e) {
            Log.e("getVersionCodeOsName", "NameNotFoundException");
        }

        return null;
    }

    public static Object[] getNetworkInfo(Context c) {
        PackageManager pm = c.getPackageManager();
        int accessNetworkState = pm.checkPermission("android.permission.ACCESS_NETWORK_STATE", c.getPackageName());
        String connectionType = UNKNOWN;
        String connectionQuality = "10";

        if (accessNetworkState == 0) {
            connectionType = getConnectionType(c);
        }

        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = TongDaoCheckTool.isEmpty(tm.getNetworkOperatorName()) ? UNKNOWN : tm.getNetworkOperatorName();
        String carrierCodeStr = TongDaoCheckTool.isEmpty(tm.getNetworkOperatorName()) ? UNKNOWN : tm.getSimOperator();

        Object realCarrierCode = 0;
        if (!TongDaoCheckTool.isEmpty(carrierCodeStr)) {
            try {
                realCarrierCode = resolveCarrierCode(carrierCodeStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return new Object[]{connectionType, connectionQuality, carrierName,
                realCarrierCode};
    }

    private static String getConnectionType(Context context) {
        int networkStatePermission = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) {
                return CONNECTION_TYPE_UNKNOWN;
            }
            int netType = info.getType();
            int netSubtype = info.getSubtype();
            if (netType == ConnectivityManager.TYPE_WIFI) {
                return CONNECTION_TYPE_WIFI;
            } else if (netType == 6) {
                return CONNECTION_TYPE_WIMAX;
            } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                switch (netSubtype) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return CONNECTION_TYPE_MOBILE_1xRTT;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return CONNECTION_TYPE_MOBILE_CDMA;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return CONNECTION_TYPE_MOBILE_EDGE;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return CONNECTION_TYPE_MOBILE_EVDO_0;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return CONNECTION_TYPE_MOBILE_EVDO_A;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return CONNECTION_TYPE_MOBILE_GPRS;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return CONNECTION_TYPE_MOBILE_UMTS;
                    default:
                        return CONNECTION_TYPE_MOBILE_UNKNOWN;
                }
            } else {
                return CONNECTION_TYPE_UNKNOWN;
            }
        } else {
            return CONNECTION_TYPE_UNKNOWN;
        }
    }

    private static Object resolveCarrierCode(String carrierCodeStr) {
        String[] carrierCodes = carrierCodeStr.split(",");
        if (carrierCodes.length == 1) {
            String code = carrierCodes[0];
            code = code.equals(UNKNOWN) ? "0" : code;
            int carrierCode = Integer.valueOf(code);
            return carrierCode;
        }

        JSONArray ary = new JSONArray();
        for (int i = 0; i < carrierCodes.length; i++) {
            ary.put(Long.parseLong(carrierCodes[i]));
        }
        return ary.toString();
    }

    public static Object[] getCurrentLocation(final Context context) {
        double latitude = 0;
        double longitude = 0;
        String source = UNKNOWN;

        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();

        int accessCoarseLocation = pm.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, packageName);
        int accessFineLocation = pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, packageName);

        if (accessCoarseLocation == 0 || accessFineLocation == 0) {

            double[] ary = getFormattedLocationString(context, accessCoarseLocation, accessFineLocation);
            if ((ary != null) && (ary.length > 1)) {
                latitude = ary[0];
                longitude = ary[1];
            }

            if (accessFineLocation == 0) {
                source = FINE;
            } else {
                if (accessCoarseLocation == 0) {
                    source = COARSE;
                }
            }
            if (latitude == 0 && longitude == 0) {
                source = UNKNOWN;
            }
        }
        return new Object[]{latitude, longitude, source};
    }

    private static double[] getFormattedLocationString(Context c, int accessCoarseLocation, int accessFineLocation) {

        double[] ary = new double[2];
        LocationManager localLocationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        Location localLocation = null;
        int i = accessCoarseLocation == 0 ? 1 : 0;
        int j = accessFineLocation == 0 ? 1 : 0;
        if ((i != 0) || (j != 0)) {
            try {
                localLocation = localLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if ((localLocation == null) && (j != 0)) {
                    localLocation = localLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (localLocation == null || (localLocation.getLatitude() == 0 && localLocation.getLongitude() == 0)) {

                        MyLocationListener locationListener = new MyLocationListener(localLocationManager);
                        localLocation = locationListener.getGPSLocation();
                    }
                    if (localLocation == null) {
                        localLocation = localLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        if (localLocation != null) {
            ary[0] = localLocation.getLatitude();
            ary[1] = localLocation.getLongitude();
        }
        return ary;
    }


    static class MyLocationListener implements LocationListener {
        Semaphore lock = new Semaphore(0);
        LocationManager locationManager;
        Location location;

        MyLocationListener(LocationManager locManager) {
            this.locationManager = locManager;
        }

        public Location getGPSLocation() {
//            Looper looper = Looper.myLooper();

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if( isGPSEnabled ) {
                Looper.prepare();
                this.locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, this);
//                try {
//                    lock.acquire();
//                } catch (InterruptedException e) {
//                    lock.release();
//                    e.printStackTrace();
//                }
            }

            return location;
        }

        @Override
        public void onLocationChanged(Location location) {
            this.location = location;
            locationManager.removeUpdates(this);
            lock.release();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            lock.release();
        }

        @Override
        public void onProviderDisabled(String s) {
            lock.release();
        }

        @Override
        public void onProviderEnabled(String s) {
            lock.release();
        }


    }

//	public static boolean isAppExist(Context context, String packageName) {
//		PackageManager packageManager = context.getPackageManager();
//		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
//		if (pinfo != null) {
//			for (PackageInfo eachPackageInfo : pinfo) {
//				if (eachPackageInfo.packageName != null&& eachPackageInfo.packageName.equals(packageName)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

//	public static void startApkByPackageName(Context context, String packageName) {
//		Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//		if (appIntent != null) {
//			appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(appIntent);
//		}
//	}

//	private static boolean isIntentCallable(Context context, Intent intent) {
//		return context.getPackageManager().queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
//	}

//	public static boolean isDeeplinkOk(Context context, String deepLink) {
//		Intent tempIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
//		return isIntentCallable(context, tempIntent);
//	}

//	public static void startLink(Context context, String link) {
//		Intent tempIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//		tempIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(tempIntent);
//	}

//	public static boolean isForeground(Context context) {
//		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//		for (RunningAppProcessInfo appProcess : appProcesses) {
//			if (appProcess.processName.equals(context.getPackageName())) {
//				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//					return true;
//				} else {
//					return false;
//				}
//			}
//		}
//		return false;
//	}

//	public static boolean isServiceRunning(Context mContext) {
//		String packageName = mContext.getPackageName();
//		boolean isRunning = false;
//		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(600);
//		if (!(serviceList.size() > 0)) {
//			return isRunning;
//		}
//
//		for (int i = 0; i < serviceList.size(); i++) {
//			if (serviceList.get(i).service.getPackageName().equals(packageName)) {
//				return true;
//			}
//		}
//		return isRunning;
//	}

    public static void getImeiInfos(final Context appContext, final JSONObject jsonObject) {
        PackageManager pm = appContext.getPackageManager();
        String packageName = appContext.getPackageName();

        int phoneStatePermission = pm.checkPermission(Manifest.permission.READ_PHONE_STATE, packageName);

        if (phoneStatePermission == 0) {
            addObject(appContext, jsonObject);
        }
    }

    private static void addObject(final Context appContext, final JSONObject jsonObject) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            String imeiMD5 = getMD5(imei);
            String imeiSha1 = getSHA1(imei);

            jsonObject.put("!imei", imei);
            jsonObject.put("!imei_md5", imeiMD5);
            jsonObject.put("!imei_sha1", imeiSha1);
        } catch (JSONException ex) {
            LOCK.release();
            ex.printStackTrace();
        }
    }

    public static String[] getMacInfos() {
        String mac = getMACAddress("wlan0"); //using wifi available
        if (mac == null) {
            mac = getMACAddress("eth0"); //using ethernet connection availale
        }

        String macMD5 = getMD5(mac);
        String macSha1 = getSHA1(mac);
        return new String[]{mac, macMD5, macSha1};
    }

    public static String[] getUdidInfos(Context appContext) {
        String androidId = Secure.getString(appContext.getContentResolver(), Secure.ANDROID_ID);
        String androidMD5 = getMD5(androidId);
        String androidSha1 = getSHA1(androidId);
        return new String[]{androidId, androidMD5, androidSha1};
    }

    private static String getMD5(String data) {
        if (data == null) {
            return null;
        }

        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    private static String getSHA1(String val) {
        if (val == null) {
            return null;
        }

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, SHA-1 should be supported?", e);
        }

        md5.update(val.getBytes());
        byte[] m = md5.digest();// 加密

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m.length; i++) {
            sb.append(Integer.toString((m[i] & 0xFF) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    @SuppressLint("NewApi")
    private static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return null;
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {

        }

        return null;
    }


    public static String getGaid(Context appContext) {
        try {
            Class<?> adIdClientClass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            Method getAdvertisingIdInfoMethod = adIdClientClass.getDeclaredMethod("getAdvertisingIdInfo", Context.class);
            Object instance = getAdvertisingIdInfoMethod.invoke(null, appContext);
            if (instance != null) {
                Class<?> targetClass = instance.getClass();
                Method getIdMethod = targetClass.getDeclaredMethod("getId");
                Object o = getIdMethod.invoke(instance);
                if (o != null) {
                    return o.toString();
                }
            }
        } catch (ClassNotFoundException e) {
            Log.d("gaid", "ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.d("gaid", "NoSuchMethodException");
        } catch (IllegalAccessException e) {
            Log.d("gaid", "IllegalAccessException");
        } catch (IllegalArgumentException e) {
            Log.d("gaid", "IllegalArgumentException");
        } catch (InvocationTargetException e) {
            Log.d("gaid", "InvocationTargetException");
        }

        return null;
    }

}
