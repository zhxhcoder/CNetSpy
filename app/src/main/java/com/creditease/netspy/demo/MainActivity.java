package com.creditease.netspy.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.creditease.netspy.demo.netspy.MainNetSpyActivity;

/**
 * Created by zhxh on 2019/06/20
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.spyButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainNetSpyActivity.class)));
    }
}
