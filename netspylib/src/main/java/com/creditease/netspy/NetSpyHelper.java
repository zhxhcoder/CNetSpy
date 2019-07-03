package com.creditease.netspy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.creditease.netspy.inner.ui.NetSpyHomeActivity;

/**
 * Created by zhxh on 2018/11/12
 * NetSpyHelper 工具类.
 */
public final class NetSpyHelper {
    static boolean isNetSpy = false;
    public static Application netSpyApp;

    public static void install(Application netSpyApp) {
        NetSpyHelper.netSpyApp = netSpyApp;
    }

    public static void debug(boolean isNetSpy) {
        NetSpyHelper.isNetSpy = isNetSpy;
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