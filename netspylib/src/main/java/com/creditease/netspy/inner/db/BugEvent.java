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
    private Long timeStamp; //时间戳

    private java.util.Date crashDate;

    private String bugSummary;
    private String bugReport;
    private String deviceInfo;
    private String userInfo;

    @Keep()
    public BugEvent(Long _id, Long timeStamp, java.util.Date crashDate,
                    String bugSummary, String bugReport, String deviceInfo, String userInfo) {
        this._id = _id;
        this.timeStamp = timeStamp;
        this.crashDate = crashDate;
        this.bugSummary = bugSummary;
        this.bugReport = bugReport;
        this.deviceInfo = deviceInfo;
        this.userInfo = userInfo;
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

    public Long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public java.util.Date getCrashDate() {
        return this.crashDate;
    }

    public void setCrashDate(java.util.Date crashDate) {
        this.crashDate = crashDate;
    }

    public String getBugReport() {
        return this.bugReport;
    }

    public void setBugReport(String bugReport) {
        this.bugReport = bugReport;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getBugSummary() {
        return bugSummary;
    }

    public void setBugSummary(String bugSummary) {
        this.bugSummary = bugSummary;
    }

    @Override
    public String toString() {
        return "BugEvent{" +
            "_id=" + _id +
            ", timeStamp=" + timeStamp +
            ", crashDate=" + crashDate +
            ", bugSummary='" + bugSummary + '\'' +
            ", bugReport='" + bugReport + '\'' +
            ", deviceInfo='" + deviceInfo + '\'' +
            ", userInfo='" + userInfo + '\'' +
            '}';
    }
}
