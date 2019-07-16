package com.creditease.netspy.demo.error;

import android.app.Application;
import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhxh on 2019/07/16
 */
public class CExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler exceptionHandler;
    private Application app;

    public CExceptionHandler(Application app) {
        this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }

    public void uncaughtException(Thread t, Throwable e) {
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
            FileOutputStream trace = app.openFileOutput("stack.trace",
                Context.MODE_PRIVATE);
            trace.write(report.toString().getBytes());
            trace.close();
        } catch (IOException ioe) {
            //TODO
        }

        exceptionHandler.uncaughtException(t, e);
    }
}
