package com.creditease.netspy.demo;

import android.app.Application;

import com.creditease.netspy.BugSpyHelper;
import com.creditease.netspy.NetSpyHelper;

import java.net.Socket;

/**
 * Created by zhxh on 2019/06/24
 */
public class SocketApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetSpyHelper.install(this);
        BugSpyHelper.debug(BuildConfig.DEBUG);
    }

    private Socket socket = null;

    public Socket getSocket(){
        return socket;
    }

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    public String getRemoteHost(){
        return socket.getInetAddress().getHostAddress();
    }

    public String getLocalHost(){
        return socket.getLocalAddress().getHostAddress();
    }
}
