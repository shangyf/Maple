package com.seven.common.act;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.seven.common.utils.status.SystemStatusManager;

/**
 * Created by SeVen on 3/8/21 9:56 AM
 */
public abstract class BaseActivity extends AppCompatActivity {

    private KProgressHUD kProgressHUD;

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

        try {
            init();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    protected abstract int getLayoutId();

    protected abstract void init() throws Throwable;

    protected View onCreateDataBinding() {
        return null;
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