package com.creditease.netspy.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.creditease.netspy.demo.error.CExceptionHelper;
import com.creditease.netspy.demo.netspy.SampleNetSpyActivity;

/**
 * Created by zhxh on 2019/06/20
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.spyButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SampleNetSpyActivity.class)));

        findViewById(R.id.errorButton).setOnClickListener(v -> {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int a = 1 / 0;

                }
            });

            String str = null;
            str.substring(2);

        });

        findViewById(R.id.sendButton).setOnClickListener(v -> CExceptionHelper.send(MainActivity.this));

    }
}
