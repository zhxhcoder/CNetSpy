package com.creditease.netspy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.ui.netspy.NetSpyListActivity;

import java.util.Date;

/**
 * Created by zhxh on 2018/11/12
 * NetSpyHelper 工具类.
 */
public final class NetSpyHelper {
    public static String source = "";
    static boolean isNetSpy = false;
    public static Application netSpyApp;

    public static void install(Application netSpyApp) {
        NetSpyHelper.install(netSpyApp, "");
    }

    public static void install(Application netSpyApp, String source) {
        NetSpyHelper.source = source;
        NetSpyHelper.netSpyApp = netSpyApp;
        //注册异常监控
        BugSpyHelper.install(netSpyApp);
    }

    /**
     * @param isNetSpy 是否 和网络监听
     */
    public static void debug(boolean isNetSpy) {
        NetSpyHelper.isNetSpy = isNetSpy;
    }

    public static boolean debug() {
        return NetSpyHelper.isNetSpy;
    }


    public static Intent launchIntent(Context context) {
        return new Intent(context, NetSpyListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void launchActivity(Context context) {
        if (NetSpyHelper.isNetSpy) {
            context.startActivity(new Intent(context, NetSpyListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            return;
        }
    }

    //加入event 例如api/reward/fee
    public static void insertHttpEvent(String method, String url, String response) {
        insertHttpEvent(NetSpyHelper.source, method, url, response);
    }

    public static void insertHttpEvent(String source, String method, String url, String response) {
        Uri uri = Uri.parse(url);
        HttpEvent transaction = new HttpEvent();
        transaction.setRequestDate(new Date());
        transaction.setSource(source);
        transaction.setMethod(method);
        transaction.setUrl(url.replaceFirst("\\?.+$", ""));
        transaction.setPath(uri.getPath());
        transaction.setTransId(System.currentTimeMillis());
        transaction.setRequestBody(uri.getQuery());
        transaction.setResponseBody(response);
        DBHelper.getInstance().insertHttpData(transaction);
    }
}