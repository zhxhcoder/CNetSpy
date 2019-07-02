package com.creditease.netspy.internal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.internal.db.DBManager;
import com.creditease.netspy.internal.db.HttpEvent;
import com.creditease.netspy.internal.support.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import static com.creditease.netspy.internal.ui.NetworkResponseFragment.TYPE_REQUEST;
import static com.creditease.netspy.internal.ui.NetworkResponseFragment.TYPE_RESPONSE;

public class NetworkTabActivity extends BaseNetSpyActivity {

    private static final String ARG_TRANSACTION_ID = "transaction_id";

    private static int selectedTabPosition = 0;

    public static void start(Context context, long transactionId) {
        Intent intent = new Intent(context, NetworkTabActivity.class);
        intent.putExtra(ARG_TRANSACTION_ID, transactionId);
        context.startActivity(intent);
    }

    TextView title;
    Adapter adapter;

    private long transactionId;
    private HttpEvent httpEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_activity_network_tab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = findViewById(R.id.toolbar_title);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        transactionId = getIntent().getLongExtra(ARG_TRANSACTION_ID, 0);

        httpEvent = DBManager.getInstance().getDataByTransId(transactionId);

        populateUI(httpEvent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.netspy_transaction, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_text) {
            share(FormatUtils.getShareResponseText(this, httpEvent));
            return true;
        } else if (item.getItemId() == R.id.share_curl) {
            share(FormatUtils.getShareCurlCommand(httpEvent));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void populateUI(HttpEvent event) {
        if (event != null) {
            title.setText(event.getMethod() + " " + event.getPath());
            for (INetworkTabFragment fragment : adapter.fragments) {
                fragment.httpTransUpdate(event);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new NetworkOverviewFragment(), getString(R.string.netspy_overview));
        adapter.addFragment(NetworkResponseFragment.newInstance(TYPE_REQUEST), getString(R.string.netspy_request));
        adapter.addFragment(NetworkResponseFragment.newInstance(TYPE_RESPONSE), getString(R.string.netspy_response));
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectedTabPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(selectedTabPosition);
    }

    private void share(String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, null));
    }

    static class Adapter extends FragmentPagerAdapter {
        final List<INetworkTabFragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(INetworkTabFragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}
