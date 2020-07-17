package com.creditease.netspy.inner.ui.bugspy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;
import com.creditease.netspy.inner.support.DeviceInfoHelper;
import com.creditease.netspy.inner.support.FormatHelper;

/**
 * Created by zhxh on 2020/05/30
 */
public class BugSpyDetailActivity extends AppCompatActivity {
    private static final String ARG_BUG_EVENT = "arg_bug_event";

    private TextView time;
    private TextView report;
    private TextView device;
    private TextView user;
    private TextView app;

    public static void start(Context context, BugEvent bugEvent) {
        Intent intent = new Intent(context, BugSpyDetailActivity.class);
        intent.putExtra(ARG_BUG_EVENT, bugEvent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_bug_spy_detail);

        time = findViewById(R.id.time);
        report = findViewById(R.id.report);
        device = findViewById(R.id.device);
        user = findViewById(R.id.user);
        app = findViewById(R.id.app);

        BugEvent bugEvent = (BugEvent) getIntent().getSerializableExtra(ARG_BUG_EVENT);

        applyUI(bugEvent);
    }

    private void applyUI(BugEvent bugEvent) {
        if (bugEvent == null) {
            return;
        }
        String strTime = FormatHelper.getHHmmSS(bugEvent.getCrashDate());
        device.setText(DeviceInfoHelper.getInstance().getAllDeviceInfo(this));
        user.setText(bugEvent.getUser());
        app.setText(bugEvent.getApp());
        time.setText("崩溃发生时间：" + strTime + "\n");
        report.setText(bugEvent.getReport());
    }
}