package com.creditease.netspy.inner.support;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.creditease.netspy.DBHelper;

/**
 * 清空数据库
 */
public class ClearTransService extends IntentService {

    public ClearTransService() {
        super("NetSpyHelper-ClearTransService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DBHelper.getInstance().deleteAllHttpData();
        NotificationHelper.clearBuffer();
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.dismiss();
    }
}