package com.creditease.netspy;

import android.content.Context;
import android.content.Intent;

import com.creditease.netspy.inner.ui.apimock.ApiMockListActivity;

/**
 * Created by zhxh on 2020/6/6
 */
public final class ApiMockHelper {
    static boolean isApiMock = false;

    //默认baseUrl
    public static String host = "10.106.157.94";

    public static void initHost(String baseUrl) {
        ApiMockHelper.host = baseUrl;
    }

    public static void debug(boolean isApiMock) {
        ApiMockHelper.isApiMock = isApiMock;
    }

    public static void launchActivity(Context context) {
        if (ApiMockHelper.isApiMock) {
            context.startActivity(new Intent(context, ApiMockListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            return;
        }
    }
}