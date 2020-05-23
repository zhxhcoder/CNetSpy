package com.creditease.netspy.inner.ui.netspy;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.HttpEvent;

/**
 * Created by zhxh on 2019/06/12
 */
public class NetSpyListActivity extends BaseNetSpyActivity implements NetSpyListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_list);
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
        HttpTabActivity.start(this, transaction.getTransId());
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}