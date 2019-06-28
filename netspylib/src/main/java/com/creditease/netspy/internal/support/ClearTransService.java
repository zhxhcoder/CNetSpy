package com.creditease.netspy.internal.support;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.creditease.netspy.internal.data.NetSpyContentProvider;

public class ClearTransService extends IntentService {

    public ClearTransService() {
        super("NetSpyHelper-ClearTransService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getContentResolver().delete(NetSpyContentProvider.TRANSACTION_URI, null, null);
        NotificationHelper.clearBuffer();
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.dismiss();
    }
}