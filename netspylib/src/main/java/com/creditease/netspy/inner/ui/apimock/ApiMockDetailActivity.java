package com.creditease.netspy.inner.ui.apimock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.support.FormatHelper;

/**
 * Created by zhxh on 2020/06/06
 */
public class ApiMockDetailActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String ARG_API_MOCK_DATA = "api_mock_data";

    private ApiMockData data;
    private String filterText = "";

    EditText path;
    EditText resp_data;
    EditText resp_empty;
    EditText resp_error;

    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;

    public static void start(Context context, ApiMockData data) {
        Intent intent = new Intent(context, ApiMockDetailActivity.class);
        intent.putExtra(ARG_API_MOCK_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_api_mock_detail);

        data = (ApiMockData) getIntent().getSerializableExtra(ARG_API_MOCK_DATA);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        path = findViewById(R.id.path);
        resp_data = findViewById(R.id.resp_data);
        resp_empty = findViewById(R.id.resp_empty);
        resp_error = findViewById(R.id.resp_error);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);

        populateUI();
    }

    private void populateUI() {
        path.setText(data.getMockPath());

        if (!TextUtils.isEmpty(data.resp_data)) {
            resp_data.setText(FormatHelper.findSearch(Color.BLUE, FormatHelper.formatJson(data.resp_data), filterText));
        }
        if (!TextUtils.isEmpty(data.resp_empty)) {
            resp_empty.setText(FormatHelper.findSearch(Color.BLUE, FormatHelper.formatJson(data.resp_empty), filterText));
        }
        if (!TextUtils.isEmpty(data.resp_error)) {
            resp_error.setText(FormatHelper.findSearch(Color.BLUE, FormatHelper.formatJson(data.resp_error), filterText));
        }
        if ("-1".equals(data.getShowType())) {
            radio3.setChecked(true);
        } else if ("0".equals(data.getShowType())) {
            radio2.setChecked(true);
        } else {
            radio1.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.netspy_api, menu);
        MenuItem editMenuItem = menu.findItem(R.id.edit);
        editMenuItem.setVisible(true);
        MenuItem uploadMenuItem = menu.findItem(R.id.upload);
        uploadMenuItem.setVisible(true);
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
        } else if (item.getItemId() == R.id.edit) {

            return true;
        } else if (item.getItemId() == R.id.upload) {

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filterText = s;
        populateUI();
        return true;
    }
}