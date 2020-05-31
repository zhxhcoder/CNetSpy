package com.creditease.netspy.demo;

import android.app.Application;

import com.creditease.netspy.BugSpyHelper;
import com.creditease.netspy.NetSpyHelper;

/**
 * Created by zhxh on 2019/06/24
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetSpyHelper.install(this);
        BugSpyHelper.debug(BuildConfig.DEBUG);
    }
}
