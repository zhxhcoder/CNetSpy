package com.creditease.netspy.demo.support;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Method;

/**
 * Created by zhxh on 2019/07/04
 */
public class NetHelper {


    //检测GPRS是否打开
    public static boolean gprsIsOpen(Context context) {
        ConnectivityManager mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = mCM.getClass();
        Class[] argClasses = null;
        Object[] argObject = null;
        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);
            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    //开启/关闭GPRS
    public static void setGprsEnabled(Context context, boolean isEnable) {
        ConnectivityManager mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = mCM.getClass();
        Class[] argClasses = new Class[1];
        argClasses[0] = boolean.class;
        try {
            Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);
            method.invoke(mCM, isEnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
