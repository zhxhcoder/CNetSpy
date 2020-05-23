package com.creditease.netspy.inner.ui.netspy;

import com.creditease.netspy.inner.db.HttpEvent;

/**
 * Created by zhxh on 2019/07/02
 * 数据返回接口.
 */
interface IHttpTabFragment {
    void httpTransUpdate(String filterText,HttpEvent httpEvent);
}