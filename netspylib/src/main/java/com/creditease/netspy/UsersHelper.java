package com.creditease.netspy;

import android.app.Activity;

import com.creditease.netspy.inner.support.UsersSelectDialog;

/**
 * Created by zhxh on 2021/3/23
 */
public final class UsersHelper {

    public static void launchDialog(Activity context, String source, UsersSelectDialog.OnSelectListener selectListener) {
        new UsersSelectDialog(context).requestUsers(source, selectListener);
    }

    public static void launchDialog(Activity context, UsersSelectDialog.OnSelectListener selectListener) {
        new UsersSelectDialog(context).requestUsers(NetSpyHelper.source, selectListener);
    }
}