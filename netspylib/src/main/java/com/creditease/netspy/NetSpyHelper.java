package com.creditease.netspy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.creditease.netspy.internal.ui.NetSpyHomeActivity;

/**
 * Created by zhxh on 2018/11/12
 * NetSpyHelper 工具类.
 */
public class NetSpyHelper {
    static boolean isNetSpy = false;
    static Application netSpyApp;

    public static void install(Application netSpyApp, boolean isNetSpy) {
        init(netSpyApp);
        init(isNetSpy);
    }

    public static void install(Application netSpyApp) {
        install(netSpyApp, false);
    }

    public static void init(boolean isNetSpy) {
        NetSpyHelper.isNetSpy = isNetSpy;
    }

    public static void init(Application netSpyApp) {
        NetSpyHelper.netSpyApp = netSpyApp;
    }

    public static Intent launchIntent(Context context) {
        return new Intent(context, NetSpyHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void launchActivity(Context context) {
        if (NetSpyHelper.isNetSpy) {
            context.startActivity(new Intent(context, NetSpyHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            return;
        }
    }
}