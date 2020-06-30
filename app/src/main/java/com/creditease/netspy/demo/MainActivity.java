package com.creditease.netspy.demo;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.creditease.netspy.ApiMockHelper;
import com.creditease.netspy.demo.netspy.BugActivity;
import com.creditease.netspy.demo.netspy.NetActivity;
import com.creditease.netspy.demo.socket.SocketMainActivity;

/**
 * Created by zhxh on 2019/06/20
 */
public class MainActivity extends AppCompatActivity {

    Button netButton;
    Button bugButton;

    private ShakeDetector shakeDetector = null;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = ContextCompat.getSystemService(this, SensorManager.class);
        setContentView(R.layout.activity_main);

        netButton = findViewById(R.id.netButton);
        bugButton = findViewById(R.id.bugButton);
        findViewById(R.id.netButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NetActivity.class)));
        findViewById(R.id.bugButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BugActivity.class)));
        findViewById(R.id.socketButton).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SocketMainActivity.class)));
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
