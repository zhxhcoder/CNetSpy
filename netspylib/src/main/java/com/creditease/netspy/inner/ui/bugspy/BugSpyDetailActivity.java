package com.creditease.netspy.inner.ui.bugspy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.creditease.netspy.DBHelper;
import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;
import com.creditease.netspy.inner.support.FormatHelper;

/**
 * Created by zhxh on 2020/05/30
 */
public class BugSpyDetailActivity extends AppCompatActivity {
    private static final String ARG_TIME_STAMP = "time_stamp";

    private long timeStamp;
    private BugEvent bugEvent;

    private TextView time;
    private TextView summary;
    private TextView content;


    public static void start(Context context, long timeStamp) {
        Intent intent = new Intent(context, BugSpyDetailActivity.class);
        intent.putExtra(ARG_TIME_STAMP, timeStamp);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_spy_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getApplicationName());

        time = findViewById(R.id.time);
        summary = findViewById(R.id.summary);
        content = findViewById(R.id.content);

        timeStamp = getIntent().getLongExtra(ARG_TIME_STAMP, 0);
        bugEvent = DBHelper.getInstance().getBugDataByTime(timeStamp);

        populateUI(bugEvent);
    }

    private void populateUI(BugEvent bugEvent) {
        String strTime = FormatHelper.getHHmmSS(bugEvent.getCrashDate());
        time.setText(strTime);
        summary.setText(bugEvent.getBugSummary());
        content.setText(bugEvent.getBugReport());
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getString(stringId);
    }
}