package com.creditease.netspy.demo;

import android.app.Application;

import com.creditease.netspy.NetSpyHelper;

/**
 * Created by zhxh on 2019/06/24
 */
public class MyApplicaion extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetSpyHelper.install(this);
    }
}
