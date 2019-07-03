package com.creditease.netspy.inner.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.creditease.netspy.inner.support.NotificationHelper;

/**
 * Created by zhxh on 2019/06/12
 * 当进入后台时关闭通知
 */
public abstract class BaseNetSpyActivity extends AppCompatActivity {
    private static boolean inForeground;
    private NotificationHelper notificationHelper;

    public static boolean isInForeground() {
        return inForeground;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationHelper = new NotificationHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inForeground = true;
        notificationHelper.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        inForeground = false;
    }
}
