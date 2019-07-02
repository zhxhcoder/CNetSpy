package com.creditease.netspy.internal.support;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.creditease.netspy.internal.db.DBManager;

public class ClearTransService extends IntentService {

    public ClearTransService() {
        super("NetSpyHelper-ClearTransService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DBManager.getInstance().deleteAllData();
        NotificationHelper.clearBuffer();
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.dismiss();
    }
}