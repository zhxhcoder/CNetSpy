package com.creditease.netspy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.creditease.netspy.inner.support.UsersSelectDialog;
import com.creditease.netspy.inner.ui.users.UsersRecordsActivity;


/**
 * Created by zhxh on 2020/6/6
 */
public final class UsersHelper {
    public static void launchActivity(Context context) {
        context.startActivity(new Intent(context, UsersRecordsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchDialog(Activity context, String source, UsersSelectDialog.OnSelectListener selectListener) {
        new UsersSelectDialog(context).requestUsers(source, selectListener);
    }
}