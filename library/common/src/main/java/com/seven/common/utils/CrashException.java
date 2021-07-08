package com.seven.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by SeVen on 2019-11-13 11:40
 */
public class CrashException implements Thread.UncaughtExceptionHandler {
    /**
     * 是否开启日志输出,在Debug状态下开启,
     * 在Release状态下关闭以提示程序性能
     */
    public static final boolean DEBUG = true;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashException INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;


    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashException() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashException getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new CrashException();
        }

        return INSTANCE;
    }


    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     *
     * @param context
     */


    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
//			如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
//
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @return true代表处理该异常，不再向上抛异常，
     * false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
     * 简单来说就是true不会弹出那个错误提示框，false就会弹出
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
//        final String msg = ex.getLocalizedMessage();
        final StackTraceElement[] stack = ex.getStackTrace();
        final String message = ex.getMessage();
        //使用Toast来显示异常信息
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void run() {
                Looper.prepare();
//                Toast.makeText(mContext, "程序出错啦:" + message, Toast.LENGTH_LONG).show();
//                可以只创建一个文件，以后全部往里面append然后发送，这样就会有重复的信息，个人不推荐
                String fileName = "crash-" + System.currentTimeMillis() + ".log";
                //
                File file = new File(mContext.getExternalFilesDir("except").
                        getAbsolutePath(), fileName);
                try {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    //导出手机信息和异常信息
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                            PackageManager.GET_ACTIVITIES);
                    pw.println("应用版本：" + pi.versionName);
                    pw.println("应用版本号：" + pi.getLongVersionCode());
                    pw.println("android版本号：" + Build.VERSION.RELEASE);
                    pw.println("android版本号API：" + Build.VERSION.SDK_INT);
                    pw.println("手机型号：" + Build.MODEL);
                    ex.printStackTrace(pw);
                    //关闭输出流
                    pw.close();
//                    FileOutputStream fos = new FileOutputStream(file, true);
//                    fos.write(message.getBytes());
//                    for (int i = 0; i < stack.length; i++) {
//                        fos.write(stack[i].toString().getBytes());
//                    }
//                    fos.flush();
//                    fos.close();
                    //这里发送一个请求获取阿里云的一些数据然后将异常上传到服务器
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }

        }.start();
        return false;
    }

    // TODO 使用HTTP Post 发送错误报告到服务器  这里不再赘述
//    private void postReport(File file) {
//      在上传的时候还可以将该app的version，该手机的机型等信息一并发送的服务器，
//      Android的兼容性众所周知，所以可能错误不是每个手机都会报错，还是有针对性的去debug比较好
//    }
}
