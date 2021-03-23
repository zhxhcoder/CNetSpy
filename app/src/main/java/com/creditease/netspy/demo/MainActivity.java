package com.creditease.netspy.demo;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.creditease.netspy.ApiMockHelper;
import com.creditease.netspy.UsersHelper;
import com.creditease.netspy.demo.netspy.BugActivity;
import com.creditease.netspy.demo.netspy.NetActivity;
import com.creditease.netspy.demo.socket.SocketMainActivity;
import com.creditease.netspy.inner.support.OkHttpHelper;
import com.creditease.netspy.inner.support.UsersSelectDialog;

/**
 * Created by zhxh on 2019/06/20
 */
public class MainActivity extends AppCompatActivity {

    Button userButton;
    Button netButton;
    Button bugButton;
    Button trackButton;

    private ShakeDetector shakeDetector = null;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = ContextCompat.getSystemService(this, SensorManager.class);
        setContentView(R.layout.activity_main);

        userButton = findViewById(R.id.userButton);
        netButton = findViewById(R.id.netButton);
        bugButton = findViewById(R.id.bugButton);
        trackButton = findViewById(R.id.trackButton);
        findViewById(R.id.userButton).setOnClickListener(v -> UsersHelper.launchDialog(this, "", new UsersSelectDialog.OnSelectListener() {
            @Override
            public void onUser(String name, String pwd) {
                Toast.makeText(MainActivity.this, "" + name + "  " + pwd, Toast.LENGTH_SHORT).show();
            }
        }));
        findViewById(R.id.netButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NetActivity.class)));
        findViewById(R.id.bugButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BugActivity.class)));
        findViewById(R.id.socketButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SocketMainActivity.class)));

        trackButton.setOnClickListener(v -> {
            OkHttpHelper.getInstance().postTrackRecords("", "", "action11", "report11", null);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OkHttpHelper.getInstance().postTrackRecords("", "", "action22", "report22", null);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OkHttpHelper.getInstance().postTrackRecords("", "", "action33", "report33", null);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OkHttpHelper.getInstance().postTrackRecords("", "", "action44", "report44", null);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setSensShake();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopSensShake();
    }

    private void stopSensShake() {
        if (shakeDetector != null) {
            shakeDetector.stop();
        }
    }

    private void setSensShake() {
        shakeDetector = new ShakeDetector(() -> {
            Toast.makeText(this, "别摇了", Toast.LENGTH_SHORT).show();
            ApiMockHelper.launchActivity(this);
        });
        shakeDetector.start(sensorManager);
    }
}
