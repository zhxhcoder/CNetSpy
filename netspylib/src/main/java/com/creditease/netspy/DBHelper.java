package com.creditease.netspy;

import android.database.sqlite.SQLiteDatabase;

import com.creditease.netspy.inner.db.DaoMaster;
import com.creditease.netspy.inner.db.DaoSession;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.db.HttpEventDao;

import java.util.List;
import java.util.concurrent.Callable;


/**
 * Created by zhxh on 2019/07/02
 * 数据库操作类.
 */
public final class DBHelper {

    private static final String DB_NAME = "cnetspy205.db"; // 数据库名称

    private static volatile DBHelper sInstance = null; // 单例

    private SQLiteDatabase mDatabase;

    private DaoSession mDaoSession;

    public DBHelper() {
        mDatabase = new DaoMaster.DevOpenHelper(NetSpyHelper.netSpyApp, DB_NAME).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(mDatabase);//用于创建数据库以及获取DaoSession
        mDaoSession = daoMaster.newSession();//用于获取各个表对应的Dao类
    }

    public static DBHelper getInstance() {
        if (sInstance == null) {
            synchronized (DBHelper.class) {
                if (sInstance == null) {
                    sInstance = new DBHelper();
                }
            }
        }
        return sInstance;
    }

    public void deleteAllData() {
        mDaoSession.deleteAll(HttpEvent.class);
    }

    public List<HttpEvent> getAllData() {
        return mDaoSession.loadAll(HttpEvent.class);
    }

    public HttpEvent getDataByTransId(long transId) {
        for (HttpEvent data : getAllData()) {
            if (transId == data.getTransId()) {
                return data;
            }
        }
        return null;
    }

    public void insertData(HttpEvent httpEvent) {
        if (httpEvent != null) {
            mDaoSession.getHttpEventDao().insertOrReplace(httpEvent);
        }
    }

    public void updateData(HttpEvent httpEvent) {
        if (httpEvent != null) {
            mDaoSession.getHttpEventDao().update(httpEvent);
        }
    }

    public List<HttpEvent> queryEventList(int responseCode) {
        return mDaoSession.callInTxNoException(new Callable<List<HttpEvent>>() {
            @Override
            public List<HttpEvent> call() throws Exception {
                return mDaoSession
                    .queryBuilder(HttpEvent.class)
                    .where(HttpEventDao.Properties.ResponseCode.eq(responseCode))
                    .list();
            }
        });
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void setDatabase(SQLiteDatabase database) {
        mDatabase = database;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        mDaoSession = daoSession;
    }

}