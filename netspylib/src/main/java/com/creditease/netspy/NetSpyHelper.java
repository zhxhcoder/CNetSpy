package com.creditease.netspy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.UsersSelectDialog;
import com.creditease.netspy.inner.ui.netspy.NetSpyListActivity;

import java.util.Date;

/**
 * Created by zhxh on 2018/11/12
 * NetSpyHelper 工具类.
 */
public final class NetSpyHelper {
    static boolean isNetSpy = false;

    private static Application netSpyApp;
    private static String source = "";

    public static void install(Application netSpyApp) {
        NetSpyHelper.install(netSpyApp, NetSpyHelper.getSource(), "", "");
    }

    public static void install(Application netSpyApp, String source) {
        NetSpyHelper.install(netSpyApp, source, "", "");
    }

    //总初始化
    public static void install(Application netSpyApp, String source, String baseURL, String strParamSet) {
        NetSpyHelper.setSource(source);
        NetSpyHelper.setSpyApp(netSpyApp);

        //BugSpyHelper注册异常监控的初始化
        BugSpyHelper.install(netSpyApp);

        //ApiMockHelper相关参数的初始化
        ApiMockHelper.initBaseURL(baseURL);
        ApiMockHelper.initParamSet(strParamSet);
    }

    public static Application getSpyApp() {
        return netSpyApp;
    }

    public static void setSpyApp(Application netSpyApp) {
        NetSpyHelper.netSpyApp = netSpyApp;
    }

    public static String getSource() {
        return source;
    }

    public static void setSource(String source) {
        NetSpyHelper.source = source;
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

    /*********************************************http相关***************************************/

    //加入event 例如api/reward/fee
    public static void insertHttpEvent(String method, String url, String response) {
        insertHttpEvent(NetSpyHelper.getSource(), method, url, response);
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

    /*********************************************users相关***************************************/

    public static void launchUsersDialog(Activity context, String source, UsersSelectDialog.OnSelectListener selectListener) {
        new UsersSelectDialog(context).requestUsers(source, selectListener);
    }

    public static void launchUsersDialog(Activity context, UsersSelectDialog.OnSelectListener selectListener) {
        new UsersSelectDialog(context).requestUsers(NetSpyHelper.getSource(), selectListener);
    }
}