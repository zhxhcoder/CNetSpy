package com.creditease.netspy.inner.ui;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.HttpEvent;

/**
 * Created by zhxh on 2019/06/12
 */
public class NetSpyHomeActivity extends BaseNetSpyActivity implements NetSpyListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getApplicationName());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, NetSpyListFragment.newInstance())
                .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(HttpEvent transaction) {
        NetworkTabActivity.start(this, transaction.getTransId());
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}
