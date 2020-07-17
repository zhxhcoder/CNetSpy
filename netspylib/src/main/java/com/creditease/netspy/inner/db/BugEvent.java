package com.creditease.netspy.inner.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhxh on 2019/07/16
 */

@Entity
public class BugEvent {
    @Id(autoincrement = true)
    private Long _id; // 自增id
    private String timestamp; //时间戳
    private java.util.Date crashDate;
    private String summary;
    private String report;
    private String device;
    private String user;
    private String app;

    @Keep()
    public BugEvent(Long _id, String timestamp, java.util.Date crashDate,
                    String summary, String report, String device, String user, String app) {
        this._id = _id;
        this.timestamp = timestamp;
        this.crashDate = crashDate;
        this.summary = summary;
        this.report = report;
        this.device = device;
        this.user = user;
        this.app = app;
    }

    @Keep()
    public BugEvent() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public Long getTimeStamp() {
        try {
            return Long.valueOf(this.timestamp);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public java.util.Date getCrashDate() {
        if (this.crashDate == null) {
            return new java.util.Date();
        }
        return this.crashDate;
    }

    public String getApp() {
        if (app == null) {
            return "";
        }
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setCrashDate(java.util.Date crashDate) {
        this.crashDate = crashDate;
    }

    public String getReport() {
        return this.report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getDevice() {
        if (device == null) {
            return "";
        }
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUser() {
        if (user == null) {
            return "";
        }
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "BugEvent{" +
                "_id=" + _id +
                ", timeStamp=" + timestamp +
                ", crashDate=" + crashDate +
                ", bugSummary='" + summary + '\'' +
                ", bugReport='" + report + '\'' +
                ", deviceInfo='" + device + '\'' +
                ", userInfo='" + user + '\'' +
                ", appInfo='" + app + '\'' +
                '}';
    }
}
