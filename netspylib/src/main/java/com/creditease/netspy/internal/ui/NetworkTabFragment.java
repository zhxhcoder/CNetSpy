package com.creditease.netspy.internal.ui;

import com.creditease.netspy.internal.data.HttpTransaction;

interface NetworkTabFragment {
    void transactionUpdated(HttpTransaction transaction);
}