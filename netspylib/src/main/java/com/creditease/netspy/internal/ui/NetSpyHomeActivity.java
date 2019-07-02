package com.creditease.netspy.internal.ui;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.creditease.netspy.R;
import com.creditease.netspy.internal.db.HttpEvent;

/**
 * Created by zhxh on 2019/06/12
 */
public class NetSpyHomeActivity extends BaseNetSpyActivity implements NetworkListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getApplicationName());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, NetworkListFragment.newInstance())
                .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(HttpEvent transaction) {
        NetworkTabActivity.start(this, transaction.get_id());
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}
