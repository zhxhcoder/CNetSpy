package com.creditease.netspy.inner.ui.netspy;

import com.creditease.netspy.inner.data.HttpTransaction;

interface NetworkTabFragment {
    void transactionUpdated(HttpTransaction transaction);
}