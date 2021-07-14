package com.seven.common.act;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.seven.common.app.BaseApplication;
import com.seven.common.utils.ToastUtil;
import com.seven.common.utils.status.SystemStatusManager;
import com.socks.library.KLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SeVen on 3/8/21 9:56 AM
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private KProgressHUD kProgressHUD;

    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        View rootView = onCreateDataBinding();
        if (null == rootView) {
            setContentView(getLayoutId());
        } else {
            setContentView(rootView);
        }

        ((BaseApplication) getApplication()).addActivity(this);

        try {
            init();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            KLog.d("------初始化失败------");
        }
    }

    protected abstract int getLayoutId();

    protected abstract void init() throws Throwable;

    protected View onCreateDataBinding() {
        return null;
    }

    /**
     * 刷新
     */
    public void onRefresh() {
        request();
    }

    /**
     * 请求
     */
    public void request() {
    }

    /**
     * 请求数据失败
     */
    public void onSuccess() {
        cancelLoading();
    }

    /**
     * 请求数据成功
     */
    public void onFailure(String msg) {
        cancelLoading();
        showToastFailure(msg);
        KLog.e(TAG, msg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelLoading();
    }

    /**
     * 设置状态栏背景状态
     */
    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);//状态栏无背景
    }

    /**
     * 显示软键盘
     */
    public void showKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * toast成功数据
     */
    public void showToastSuccess(String msg) {
        ToastUtil.showToast(this, msg, ToastUtil.ToastType.SUCCESS);
    }

    /**
     * toast失败消息
     *
     * @param msg
     */
    public void showToastFailure(String msg) {
        ToastUtil.showToast(this, msg, ToastUtil.ToastType.FAILURE);
    }

    /**
     * 显示加载进度条
     */
    public void showLoading(String msg) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(this);
            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
            kProgressHUD.setCancellable(true);
            kProgressHUD.setAnimationSpeed(2);
            kProgressHUD.setDimAmount(0.5f);
        }
        kProgressHUD.setDetailsLabel(msg);
        kProgressHUD.show();
    }

    public void cancelLoading() {
        if (kProgressHUD != null && kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
    }


}