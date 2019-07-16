package com.creditease.netspy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zhxh on 2019/07/16
 */
public class ErrorSpyHelper implements Thread.UncaughtExceptionHandler {
    static boolean isErrorSpy = false;

    private Thread.UncaughtExceptionHandler exceptionHandler;
    private Application app;

    public ErrorSpyHelper(Application app) {
        this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }

    public static void install(Application app) {
        Thread.setDefaultUncaughtExceptionHandler(new ErrorSpyHelper(app));
    }

    /**
     * @param isErrorSpy 是否进行异常监听
     */
    public static void debug(boolean isErrorSpy) {
        ErrorSpyHelper.isErrorSpy = isErrorSpy;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!NetSpyHelper.isNetSpy) {
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
            FileOutputStream trace = app.openFileOutput("stack.trace",
                Context.MODE_PRIVATE);
            trace.write(report.toString().getBytes());
            trace.close();
        } catch (IOException ioe) {
            //TODO
        }
        exceptionHandler.uncaughtException(t, e);
    }


    public static void sendEmail(Activity activity) {
        StringBuilder trace = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(activity.openFileInput("stack.trace")));

            while ((line = reader.readLine()) != null) {
                trace.append(line).append("\n");
            }
        } catch (FileNotFoundException fnf) {
            //TODO
        } catch (IOException ioe) {
            //TODO
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subject = "异常报告";
        String body = "异常日志记录如下: " + "\n" + trace.toString() + "\n";

        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mrcoder@qq.com"});
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("message/rfc822");

        activity.startActivity(Intent.createChooser(sendIntent, "Title:"));

        activity.deleteFile("stack.trace");
    }
}
