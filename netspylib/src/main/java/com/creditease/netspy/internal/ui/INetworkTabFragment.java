package com.creditease.netspy.internal.ui;

import com.creditease.netspy.internal.db.HttpEvent;

interface INetworkTabFragment {
    void httpTransUpdate(HttpEvent httpEvent);
}