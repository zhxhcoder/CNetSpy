package com.creditease.netspy.inner.ui.netspy;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.data.HttpTransaction;

/**
 * Created by zhxh on 2019/06/12
 */
public class NetSpyMainActivity extends BaseNetSpyActivity implements NetworkListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getApplicationName());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, NetworkListFragment.newInstance())
                .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(HttpTransaction transaction) {
        NetworkTabActivity.start(this, transaction.getId());
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}