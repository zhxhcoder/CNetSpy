package com.creditease.netspy.inner.support;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhxh on 2020/5/30
 */
public class DeviceInfoHelper {

    private static final String TAG = "DeviceInfoHelper";
    private static DeviceInfoHelper instance;

    public DeviceInfoHelper() {
    }


    public static DeviceInfoHelper getInstance() {

        if (instance == null) {
            instance = new DeviceInfoHelper();
        }
        return instance;
    }

    public String getAllDeviceInfo(Activity context) {
        StringBuilder info = new StringBuilder();

        info.append("\n");
        info.append("手机型号：" + getPhoneModel());
        info.append("\n");
        info.append("操作系统:" + getOS());
        info.append("\n");
        info.append("联网方式:" + getNetMode(context));
        info.append("\n");
        info.append("分辨率：" + getResolution(context));
        info.append("\n");

        return info.toString();
    }

    /**
     * 获取机型
     */
    public String getPhoneModel() {
        String brand = android.os.Build.BRAND;//手机品牌
        String model = android.os.Build.MODEL;//手机型号
        Log.w(TAG, "手机型号：" + brand + " " + model);
        return brand + " " + model;
    }

    /**
     * 获取手机分辨率
     *
     * @param context
     * @return
     */
    public String getResolution(Activity context) {
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        Log.w(TAG, "分辨率：" + screenWidth + "*" + screenHeight);
        return screenWidth + "*" + screenHeight;

    }

    /**
     * 获取操作系统
     *
     * @return
     */
    public String getOS() {
        Log.w(TAG, "操作系统:" + "Android" + android.os.Build.VERSION.RELEASE);
        return "Android" + android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取联网方式
     */
    public String getNetMode(Context context) {
        String strNetworkType = "未知";
        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            int netMode = networkInfo.getType();
            if (netMode == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
                //wifi
            } else if (netMode == ConnectivityManager.TYPE_MOBILE) {
                int networkType = networkInfo.getSubtype();
                switch (networkType) {

                    //2g
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;

                    //3g
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;

                    default:
                        String _strSubTypeName = networkInfo.getSubtypeName();
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        Log.w(TAG, "联网方式:" + strNetworkType);
        return strNetworkType;
    }
}
