package com.creditease.netspy.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.creditease.netspy.demo.netspy.SampleSpyActivity;

/**
 * Created by zhxh on 2019/06/20
 */
public class MainActivity extends AppCompatActivity {

    Button spyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spyButton = findViewById(R.id.spyButton);

        findViewById(R.id.spyButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SampleSpyActivity.class)));

        findViewById(R.id.errorButton0).setOnClickListener(v -> {
            new Thread(() -> {
                spyButton.setText("子线程");
            }).start();
        });
        findViewById(R.id.errorButton1).setOnClickListener(v -> {
            new Thread(() -> {
                int a = 1 / 0;
            }).start();

            int a = 1 / 0;
        });

        findViewById(R.id.errorButton2).setOnClickListener(v -> {
            String a = "哈哈";
            int b = Integer.parseInt(a);

            //下面来不及运行就崩溃了
            new Thread(() -> {
                String aa = "哈哈";
                int bb = Integer.parseInt(aa);
            }).start();
        });

        findViewById(R.id.errorButton3).setOnClickListener(v -> {
            new Thread(() -> {
                String str = null;
                str.substring(2);
            }).start();

            String str = null;
            str.substring(2);
        });
        findViewById(R.id.errorButton4).setOnClickListener(v -> {

            try {
                Thread.sleep(32000);
                spyButton.setText("超时");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }
}
