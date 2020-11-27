package com.creditease.netspy.inner.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Created by zhxh on 2020/5/30
 */
public class DeviceInfoHelper {

    private static final String TAG = "DeviceInfoHelper";
    private static DeviceInfoHelper instance;

    private DeviceInfoHelper() {
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
        info.append("设备唯一号：" + getDeviceId(context));
        info.append("\n");
        info.append("手机型号：" + getPhoneModel());
        info.append("\n");
        info.append("操作系统：" + getOS());
        info.append("\n");
        info.append("联网方式：" + getNetMode(context));
        info.append("\n");
        info.append("分辨率：" + getResolution(context));
        info.append("\n");

        return info.toString();
    }

    //公司用的deviceId
    public static String getDeviceId(Context context) {
        // 具体逻辑为当imei和androidId和mac拼一起，如果哪项为空，就不拼了
        StringBuilder deviceIdSB = new StringBuilder();
        String imei = getImei(context);
        String androidId = getAndroidId(context);
        String mac = getMac(context);
        if (!TextUtils.isEmpty(imei) && !TextUtils.isEmpty(androidId) && !TextUtils.isEmpty(mac)) {
            deviceIdSB.append(imei);
            deviceIdSB.append("|");
            deviceIdSB.append(androidId);
            deviceIdSB.append("|");
            deviceIdSB.append(mac);
        } else if (!TextUtils.isEmpty(imei) && !TextUtils.isEmpty(androidId)) {
            deviceIdSB.append(imei);
            deviceIdSB.append("|");
            deviceIdSB.append(androidId);
        } else if (!TextUtils.isEmpty(imei) && !TextUtils.isEmpty(mac)) {
            deviceIdSB.append(imei);
            deviceIdSB.append("|");
            deviceIdSB.append(mac);
        } else if (!TextUtils.isEmpty(androidId) && !TextUtils.isEmpty(mac)) {
            deviceIdSB.append(androidId);
            deviceIdSB.append("|");
            deviceIdSB.append(mac);
        } else if (!TextUtils.isEmpty(imei)) {
            deviceIdSB.append(imei);
        } else if (!TextUtils.isEmpty(androidId)) {
            deviceIdSB.append(androidId);
        } else if (!TextUtils.isEmpty(mac)) {
            deviceIdSB.append(mac);
        }

        return deviceIdSB.toString();
    }


    public static String getMac(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return getLocalMacAddressFromWifiInfo(context);
        } else {
            if (!TextUtils.isEmpty(getMacAddressByIp())) {
                return getMacAddressByIp();
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                return getMachineHardwareAddress();
            } else {
                return getLocalMacAddressFromBusybox();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        if (isPermissionGranted(context, ACCESS_WIFI_STATE)) {
            if (isAccessWifiStateAuthorized(context)) {
                WifiManager wifiManager =
                        (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
                    return wifiManager.getConnectionInfo().getMacAddress();
                }
            }
        }
        return "";
    }

    private static boolean isAccessWifiStateAuthorized(Context context) {
        return PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(
                "android.permission.ACCESS_WIFI_STATE");
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }


    public static String getMacAddressByIp() {
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            return buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 方法描述：获取移动设备本地IP
     *
     * @author renmeng
     * @time 2019-05-23
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImei(Context context) {
        if (isPermissionGranted(context, READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getDeviceId();
            }
        }
        return "";
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
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
