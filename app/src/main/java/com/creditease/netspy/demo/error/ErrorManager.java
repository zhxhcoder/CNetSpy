package com.creditease.netspy.demo.error;

import android.app.Activity;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zhxh on 2019/07/16
 */
public class ErrorManager {

    public void send(Activity activity) {
        StringBuilder trace = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(activity.openFileInput("stack.trace")));

            while ((line = reader.readLine()) != null) {
                trace.append(line).append("\n");
            }
        } catch (FileNotFoundException fnf) {
            //TODO
        } catch (IOException ioe) {
            //TODO
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subject = "Error report";
        String body = "Mail this to appdeveloper@gmail.com: " + "\n" + trace.toString() + "\n";

        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"readerscope@altcanvas.com"});
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("message/rfc822");

        activity.startActivity(Intent.createChooser(sendIntent, "Title:"));

        activity.deleteFile("stack.trace");
    }
}
