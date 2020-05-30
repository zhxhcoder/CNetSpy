package com.creditease.netspy;

import android.database.sqlite.SQLiteDatabase;

import com.creditease.netspy.inner.db.BugEvent;
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
    //只保留最多 500条 崩溃日志 1000条网络日志
    private static final int bugLogCount = 200;
    private static final int httpLogCount = 1000;

    private static final String DB_NAME = "app_spy41.db"; // 数据库名称
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

    public void deleteAllBugData() {
        mDaoSession.deleteAll(BugEvent.class);
    }

    public void deleteBugData(BugEvent bugEvent) {
        if (bugEvent != null) {
            mDaoSession.getBugEventDao().delete(bugEvent);
        }
    }

    public void insertBugData(BugEvent bugEvent) {
        if (bugEvent != null) {
            //resetBugDataSize();
            mDaoSession.getBugEventDao().insertOrReplace(bugEvent);
        }
    }

    private void resetBugDataSize() {
        //TODO 如果超过 bugLogCount数量 则删除最老加入的一条

        List<BugEvent> list = getAllBugData();
        if (list == null || list.isEmpty()) {
            return;
        }
        int size = list.size();
        if (size >= bugLogCount) {
            mDaoSession.delete(list.get(size - 1));
        }
    }

    private void resetHttpDataSize() {
        //TODO 如果超过 httpLogCount数量 则删除最老加入的一条

        List<HttpEvent> list = getAllHttpData();
        if (list == null || list.isEmpty()) {
            return;
        }
        int size = list.size();
        if (size >= httpLogCount) {
            mDaoSession.delete(list.get(size - 1));
        }
    }

    public List<BugEvent> getAllBugData() {
        return mDaoSession.loadAll(BugEvent.class);
    }

    public void deleteAllHttpData() {
        mDaoSession.deleteAll(HttpEvent.class);
    }

    public List<HttpEvent> getAllHttpData() {
        return mDaoSession.loadAll(HttpEvent.class);
    }

    public HttpEvent getHttpDataByTransId(long transId) {
        for (HttpEvent data : getAllHttpData()) {
            if (transId == data.getTransId()) {
                return data;
            }
        }
        return null;
    }

    public BugEvent getBugDataByTime(long timeStamp) {
        for (BugEvent data : getAllBugData()) {
            if (timeStamp == data.getTimeStamp()) {
                return data;
            }
        }
        return null;

    }

    public void insertHttpData(HttpEvent httpEvent) {
        if (httpEvent != null) {
            //resetHttpDataSize();
            mDaoSession.getHttpEventDao().insertOrReplace(httpEvent);
        }
    }

    public void deleteHttpData(HttpEvent httpEvent) {
        if (httpEvent != null) {
            mDaoSession.getHttpEventDao().delete(httpEvent);
        }
    }

    public void updateHttpData(HttpEvent httpEvent) {
        if (httpEvent != null) {
            mDaoSession.getHttpEventDao().update(httpEvent);
        }
    }

    public List<HttpEvent> queryHttpEventByFilter(String filterText) {
        return mDaoSession.callInTxNoException(new Callable<List<HttpEvent>>() {
            @Override
            public List<HttpEvent> call() throws Exception {
                return mDaoSession
                        .queryBuilder(HttpEvent.class)
                        .where(HttpEventDao.Properties.Path.like("%" + filterText + "%"))
                        .list();
            }
        });
    }

    public List<HttpEvent> queryHttpEventList(int responseCode) {
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
