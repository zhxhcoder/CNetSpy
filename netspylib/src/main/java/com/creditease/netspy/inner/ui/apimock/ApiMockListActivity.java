package com.creditease.netspy.inner.ui.apimock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.support.OkHttpHelper;

/**
 * Created by zhxh on 2020/06/06
 */
public class ApiMockListActivity extends AppCompatActivity {

    TextView tvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_mock_list);

        tvList = findViewById(R.id.tvList);
        downLoadFromCloud();
    }


    private void downLoadFromCloud() {
        OkHttpHelper.getInstance().getRecords(resp -> {
            tvList.setText(resp);
        });
    }
}