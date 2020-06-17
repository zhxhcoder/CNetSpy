package com.creditease.netspy.inner.ui.apimock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.support.FormatHelper;
import com.creditease.netspy.inner.support.OkHttpHelper;

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

    boolean isReadOnly = true;

    Toolbar toolbar;

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

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("API详情");
        setSupportActionBar(toolbar);

        path = findViewById(R.id.path);
        resp_data = findViewById(R.id.resp_data);
        resp_empty = findViewById(R.id.resp_empty);
        resp_error = findViewById(R.id.resp_error);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);

        applyUI();
    }


    private void applyUI() {
        path.setText(data.getMockPath());

        resp_data.setText(FormatHelper.findSearch(Color.BLUE, FormatHelper.formatJson(data.resp_data), filterText));
        resp_empty.setText(FormatHelper.findSearch(Color.BLUE, FormatHelper.formatJson(data.resp_empty), filterText));
        resp_error.setText(FormatHelper.findSearch(Color.BLUE, FormatHelper.formatJson(data.resp_error), filterText));

        if ("-1".equals(data.getShowType())) {
            radio3.setChecked(true);
        } else if ("0".equals(data.getShowType())) {
            radio2.setChecked(true);
        } else {
            radio1.setChecked(true);
        }

        radio1.setOnClickListener(v -> {
            radio1.setChecked(true);
            radio2.setChecked(false);
            radio3.setChecked(false);
        });

        radio2.setOnClickListener(v -> {
            radio1.setChecked(false);
            radio2.setChecked(true);
            radio3.setChecked(false);
        });
        radio3.setOnClickListener(v -> {
            radio1.setChecked(false);
            radio2.setChecked(false);
            radio3.setChecked(true);
        });

        path.setEnabled(false);
        resp_data.setEnabled(false);
        resp_empty.setEnabled(false);
        resp_error.setEnabled(false);
    }

    private int getShowType() {
        if (radio3.isChecked()) {
            return -1;
        } else if (radio2.isChecked()) {
            return 0;
        } else {
            return 1;
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
            toolbar.setTitle("API正在编辑中......");
            isReadOnly = false;

            path.setEnabled(true);
            resp_data.setEnabled(true);
            resp_empty.setEnabled(true);
            resp_error.setEnabled(true);

            path.setText(data.getMockPath());
            return true;
        } else if (item.getItemId() == R.id.upload) {
            if (isReadOnly) {
                Toast.makeText(ApiMockDetailActivity.this, "请先点击右上角编辑按钮", Toast.LENGTH_LONG).show();
                return true;
            }
            if (TextUtils.isEmpty(path.getText().toString())) {
                Toast.makeText(ApiMockDetailActivity.this, "path不能为空", Toast.LENGTH_LONG).show();
                return true;
            }
            if (TextUtils.isEmpty(resp_data.getText().toString()) && TextUtils.isEmpty(resp_empty.getText().toString()) && TextUtils.isEmpty(resp_error.getText().toString())) {
                Toast.makeText(ApiMockDetailActivity.this, "resp不能为空", Toast.LENGTH_LONG).show();
                return true;
            }

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("确定要添加或修改所编辑的内容")
                    .setPositiveButton("取消", null)
                    .setNegativeButton("确定", (dialog1, which) -> {

                        String strRespData = resp_data.getText().toString().replaceAll("\\s+", "");
                        String strRespEmpty = resp_empty.getText().toString().replaceAll("\\s+", "");
                        String strRespError = resp_error.getText().toString().replaceAll("\\s+", "");

                        OkHttpHelper.getInstance().postApiRecords(path.getText().toString(), getShowType(), strRespData, strRespEmpty, strRespError, handler);
                    })
                    .create();
            dialog.show();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == OkHttpHelper.SAVE_SUCCESS) {
                toolbar.setTitle("API详情");

                Toast.makeText(ApiMockDetailActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                isReadOnly = true;
                path.setEnabled(false);
                resp_data.setEnabled(false);
                resp_empty.setEnabled(false);
                resp_error.setEnabled(false);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(OkHttpHelper.SAVE_SUCCESS);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filterText = s;
        applyUI();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !isReadOnly) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("您当前已处于编辑状态，返回可能丢失编辑内容")
                    .setPositiveButton("取消", null)
                    .setNegativeButton("离开", (dialog1, which) -> finish())
                    .create();
            dialog.show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}