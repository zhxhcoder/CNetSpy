package com.creditease.netspy.internal.ui;

import com.creditease.netspy.internal.db.HttpEvent;
/**
 * Created by zhxh on 2019/07/02
 * 数据库操作类.
 */
interface INetworkTabFragment {
    void httpTransUpdate(HttpEvent httpEvent);
}