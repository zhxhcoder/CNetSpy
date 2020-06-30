package com.creditease.netspy.demo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpMainActivity extends AppCompatActivity {
    Button btnUp;
    Button btnDown;
    EditText txtAddress;

    Socket myAppSocket = null;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_main);

        btnUp = (Button) findViewById(R.id.btnUP);
        btnDown = (Button) findViewById(R.id.btnDown);

        txtAddress = (EditText) findViewById(R.id.ipAddress);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "UP";
                Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                cmd_increase_servo.execute();
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "DOWN";
                Socket_AsyncTask cmd_decrease_servo = new Socket_AsyncTask();
                cmd_decrease_servo.execute();
            }
        });

    }

    public void getIPandPort() {
        String iPandPort = txtAddress.getText().toString();
        String temp[] = iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);

        Log.d("sampleSpyLog", "IP String" + iPandPort);
        Log.d("sampleSpyLog", "IP:" + wifiModuleIp);
        Log.d("sampleSpyLog", "PORT" + wifiModulePort);

    }

    public static class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        protected Void doInBackground(Void... params) {
            try {
                InetAddress inetAdress = InetAddress.getByName(wifiModuleIp);
                socket = new java.net.Socket(inetAdress, wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}