package com.creditease.netspy.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.creditease.netspy.demo.netspy.SampleSpyActivity;

/**
 * Created by zhxh on 2019/06/20
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.spyButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SampleSpyActivity.class)));

        findViewById(R.id.errorButton1).setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //子线程崩溃没事？？？
                    int a = 1 / 0;
                }
            });
        });

        findViewById(R.id.errorButton2).setOnClickListener(v -> {
            String a = "哈哈";
            int b = Integer.parseInt(a);
        });

        findViewById(R.id.errorButton3).setOnClickListener(v -> {
            String str = null;
            str.substring(2);
        });
    }
}
