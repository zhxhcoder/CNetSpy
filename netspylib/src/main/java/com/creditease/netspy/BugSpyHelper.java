package com.creditease.netspy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.creditease.netspy.inner.ui.bugspy.BugSpyListActivity;

/**
 * Created by zhxh on 2020/05/23
 */

public class BugSpyHelper implements Thread.UncaughtExceptionHandler {

    static boolean isBugSpy = false;

    private Thread.UncaughtExceptionHandler exceptionHandler;
    private Application app;

    private BugSpyHelper(Application app) {
        this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }

    static void install(Application app) {
        Thread.setDefaultUncaughtExceptionHandler(new BugSpyHelper(app));
    }

    /**
     * @param isBugSpy 是否进行异常监听
     */
    public static void debug(boolean isBugSpy) {
        BugSpyHelper.isBugSpy = isBugSpy;
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
        report.append("--------- stacktrace-begin ---------\n\n");
        for (int i = 0; i < arr.length; i++) {
            report.append("    ").append(arr[i].toString()).append("\n");
        }
        report.append("\n\n-------------- stacktrace-end -----------------\n\n");
        /*
         * 意外是子线程时,输出在这里
         */
        Throwable cause = e.getCause();
        if (cause != null) {
            report.append("--------- thread-begin ---------\n\n");

            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report.append("    ").append(arr[i].toString()).append("\n");
            }
            report.append("-------------thread-end------------------\n\n");
        }

        try {
        } catch (Exception exec) {
        }

        exceptionHandler.uncaughtException(t, e);
    }
}