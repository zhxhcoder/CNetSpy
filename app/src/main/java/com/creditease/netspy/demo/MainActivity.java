package com.creditease.netspy.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.creditease.netspy.demo.netspy.SampleSpyActivity;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spyButton.setText("超时错误");
        });
        findViewById(R.id.errorButton5).setOnClickListener(v -> {//anr
            try {
                Thread.sleep(32000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spyButton.setText("ANR模拟");

        });

        findViewById(R.id.errorButton6).setOnClickListener(v -> {//OutOfMemory

            final List<byte[]> container = new ArrayList<>();

            Log.e("cAppSpy", "max memory = " + Runtime.getRuntime().maxMemory());
            Log.e("cAppSpy", "free memory = " + Runtime.getRuntime().freeMemory());
            Log.e("cAppSpy", "total memory = " + Runtime.getRuntime().totalMemory());
            byte[] b = new byte[100 * 1000 * 1000];
            container.add(b);

        });
        findViewById(R.id.errorButton7).setOnClickListener(v -> {//noExists
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream("aa.txt"));
                Log.e("cAppSpy", bis.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });
    }
}
