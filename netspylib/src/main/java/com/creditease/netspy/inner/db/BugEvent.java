package com.creditease.netspy.inner.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import java.util.Date;

/**
 * Created by zhxh on 2019/07/16
 */

@Entity
public class BugEvent {
    @Id(autoincrement = true)
    private Long _id; // 自增id
    private Long timeStamp; //时间戳

    private java.util.Date crashDate;

    private String bugReport;

    @Generated(hash = 1453206622)
    public BugEvent(Long _id, Long timeStamp, java.util.Date crashDate,
            String bugReport) {
        this._id = _id;
        this.timeStamp = timeStamp;
        this.crashDate = crashDate;
        this.bugReport = bugReport;
    }

    @Generated(hash = 135755314)
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
}
