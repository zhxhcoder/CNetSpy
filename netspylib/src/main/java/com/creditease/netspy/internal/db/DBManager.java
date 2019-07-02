package com.creditease.netspy.internal.db;

import android.database.sqlite.SQLiteDatabase;

import com.creditease.netspy.NetSpyHelper;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by zhxh on 2019/07/02
 * 数据库操作类.
 */
public class DBManager {

    private static final String DB_NAME = "cnetspy.db"; // 数据库名称

    private static volatile DBManager sInstance = null; // 单例

    private SQLiteDatabase mDatabase;

    private DaoSession mDaoSession;

    public DBManager() {
        mDatabase = new DaoMaster.DevOpenHelper(NetSpyHelper.netSpyApp, DB_NAME).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        mDaoSession = daoMaster.newSession();
    }

    public static DBManager getInstance() {
        if (sInstance == null) {
            synchronized (DBManager.class) {
                if (sInstance == null) {
                    sInstance = new DBManager();
                }
            }
        }
        return sInstance;
    }

    public void deleteAllData() {
        mDaoSession.deleteAll(HttpEvent.class);
    }

    public List<HttpEvent> getAllData() {
        List<HttpEvent> datas = mDaoSession.loadAll(HttpEvent.class);
        return datas;
    }

    public void insertData(HttpEvent httpEvent) {
        if (httpEvent != null) {
            mDaoSession.insertOrReplace(httpEvent);
        }
    }

    public void saveEventList(final List<HttpEvent> eventList) {
        if (eventList == null || eventList.isEmpty()) {
            return;
        }
        mDaoSession.callInTxNoException(new Callable() {

            @Override
            public Object call() throws Exception {
                for (HttpEvent httpEvent : eventList) {
                    mDaoSession.getHttpEventDao().insertOrReplace(httpEvent);
                }
                return null;
            }
        });
    }


    public void removeEventList(final List<HttpEvent> eventList) {
        if (eventList == null || eventList.isEmpty()) {
            return;
        }
        mDaoSession.callInTxNoException(new Callable() {
            @Override
            public Object call() throws Exception {
                for (HttpEvent httpEvent : eventList) {
                    mDaoSession.getHttpEventDao().delete(httpEvent);
                }
                return null;
            }
        });
    }

    public List<HttpEvent> queryEventList() {
        return mDaoSession.callInTxNoException(new Callable<List<HttpEvent>>() {
            @Override
            public List<HttpEvent> call() throws Exception {
                List<HttpEvent> eventList = mDaoSession.queryBuilder(HttpEvent.class).list();
                if (eventList != null) {
                    for (HttpEvent httpEvent : eventList) {
                        mDaoSession.insertOrReplace(httpEvent);
                    }
                }
                return eventList;
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
