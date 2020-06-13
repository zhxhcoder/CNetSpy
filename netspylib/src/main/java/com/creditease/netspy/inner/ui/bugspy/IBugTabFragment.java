package com.creditease.netspy.inner.ui.bugspy;

import com.creditease.netspy.inner.db.BugEvent;

/**
 * Created by zhxh on 2019/07/02
 * 数据返回接口.
 */
interface IBugTabFragment {
    void updateBugData(BugEvent bugEvent);
}