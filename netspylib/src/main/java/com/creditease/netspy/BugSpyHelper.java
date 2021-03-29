package com.creditease.netspy;

import android.content.Context;
import android.content.Intent;

import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.inner.db.BugEvent;
import com.creditease.netspy.inner.ui.bugspy.BugSpyListActivity;

import java.util.Date;

/**
 * Created by zhxh on 2019/07/16
 */
public final class BugSpyHelper implements Thread.UncaughtExceptionHandler {

    static boolean isBugSpy = false;

    static String appInfo = "";
    static String userInfo = "";
    static String deviceInfo = "";

    private Thread.UncaughtExceptionHandler exceptionHandler;

    private BugSpyHelper() {
        this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    static void install() {
        Thread.setDefaultUncaughtExceptionHandler(new BugSpyHelper());
    }

    /**
     * @param isBugSpy 是否进行异常监听
     */
    public static void debug(boolean isBugSpy) {
        BugSpyHelper.isBugSpy = isBugSpy;
    }

    public static boolean debug() {
        return BugSpyHelper.isBugSpy;
    }

    /**
     * @param appInfo  当时打包app信息 打包时间、打包版本号等信息
     * @param userInfo 当时用户信息 用户名 用户ID等信息
     */
    public static void initInfo(String appInfo, String userInfo) {
        BugSpyHelper.appInfo = appInfo;
        BugSpyHelper.userInfo = userInfo;
    }

    public static void initInfo(String appInfo, String userInfo, String deviceInfo) {
        BugSpyHelper.appInfo = appInfo;
        BugSpyHelper.userInfo = userInfo;
        BugSpyHelper.deviceInfo = deviceInfo;
    }

    public static void launchActivity(Context context) {
        if (BugSpyHelper.isBugSpy) {
            context.startActivity(new Intent(context, BugSpyListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            return;
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!BugSpyHelper.isBugSpy) {
            return;
        }
        StackTraceElement[] arr = e.getStackTrace();
        StringBuilder report = new StringBuilder(e.toString() + "\n\n");
        for (int i = 0; i < arr.length; i++) {
            report.append("    ").append(arr[i].toString()).append("\n");
        }
        /*
         * 意外是子线程时,输出在这里
         */
        Throwable cause = e.getCause();
        if (cause != null) {
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report.append("    ").append(arr[i].toString()).append("\n");
            }
        }
        try {
            saveError(e.toString(), report.toString());
        } catch (Exception exec) {
        }

        exceptionHandler.uncaughtException(t, e);
    }

    private void saveError(String reportTitle, String report) {
        BugEvent event = new BugEvent();
        event.setApp(BugSpyHelper.appInfo);
        event.setDevice(BugSpyHelper.deviceInfo);
        event.setUser(BugSpyHelper.userInfo);
        event.setTimestamp(String.valueOf(System.currentTimeMillis()));
        event.setCrashDate(new Date());
        event.setSummary(reportTitle);
        event.setReport(report);
        DBHelper.getInstance().insertBugData(event);
    }
}
