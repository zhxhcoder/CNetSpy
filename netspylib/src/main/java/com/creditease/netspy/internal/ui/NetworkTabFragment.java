package com.creditease.netspy.internal.ui;

import com.creditease.netspy.internal.db.HttpEvent;

interface NetworkTabFragment {
    void transactionUpdated(HttpEvent transaction);
}