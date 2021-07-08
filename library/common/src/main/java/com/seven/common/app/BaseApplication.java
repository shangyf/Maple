package com.seven.common.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.seven.common.utils.CrashException;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SeVen on 3/8/21 9:57 AM
 */
public abstract class BaseApplication extends Application {
    public static final String TAG = "BaseApplication";

    public List<Activity> allActivity = new ArrayList<>();
    //全局变量
    public static Context context;

    private static BaseApplication mInstance;

    private CrashException crashException = null;

    protected boolean isLog = true;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();

        context = getApplicationContext();
        crashException = CrashException.getInstance();
        crashException.init(context);

        KLog.init(isLog);

        mInstance = this;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static BaseApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return context;
    }

    public void addActivity(Activity activity) {
        allActivity.add(activity);
    }

    /**
     * 移除Activity
     */
    public void removeActivity(Activity activity) {
        if (null != allActivity && allActivity.contains(activity)) {
            allActivity.remove(activity);
        }
    }

    public List<Activity> getAllActivity() {
        return allActivity;
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : allActivity) {
            KLog.d(TAG, "activity:" + activity);
            if (null != activity) {
                activity.finish();
            }
        }
    }
}