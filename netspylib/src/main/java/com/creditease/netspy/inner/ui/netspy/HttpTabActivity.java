package com.creditease.netspy.inner.ui.netspy;

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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.creditease.netspy.DBHelper;
import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.FormatHelper;

import java.util.ArrayList;
import java.util.List;

public class HttpTabActivity extends BaseNetSpyActivity implements
        SearchView.OnQueryTextListener {

    private String filterText = "";

    private static final String ARG_TRANS_ID = "trans_id";

    private static int selectedTabPosition = 0;

    public static void start(Context context, long transId) {
        Intent intent = new Intent(context, HttpTabActivity.class);
        intent.putExtra(ARG_TRANS_ID, transId);
        context.startActivity(intent);
    }

    Adapter adapter;

    private long transactionId;
    private HttpEvent httpEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_activity_http_tab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        transactionId = getIntent().getLongExtra(ARG_TRANS_ID, 0);

        httpEvent = DBHelper.getInstance().getHttpDataByTransId(transactionId);

        populateUI(httpEvent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.netspy_network_tab_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            return true;
        } else if (item.getItemId() == R.id.share_text) {
            share(httpEvent.getFormattedRequestBody() + "\n\n" + FormatHelper.getShareResponseText(this, httpEvent));
            return true;
        } else if (item.getItemId() == R.id.share_curl) {
            share(FormatHelper.getShareCurlCommand(httpEvent));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void populateUI(HttpEvent event) {
        if (event != null) {
            for (IHttpTabFragment fragment : adapter.fragments) {
                fragment.httpTransUpdate(filterText, event);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(HttpResponseFragment.newInstance(), getString(R.string.netspy_response));
        adapter.addFragment(new HttpOverviewFragment(), getString(R.string.netspy_overview));
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filterText = s;
        populateUI(httpEvent);
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        final List<IHttpTabFragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(IHttpTabFragment fragment, String title) {
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
