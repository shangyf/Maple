package com.seven.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.seven.basic.R;


/**
 * Created by SeVen on 3/8/21 10:07 AM
 */
public abstract class BaseDialog<T> extends Dialog implements View.OnClickListener {
    protected Context context;

    private int width;
    private int height;

    public T t;

    public BaseDialog(Context context) {
        this(context, R.style.Theme_dialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        width = setWidth();
        height = setHeight();

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = height;
        dialogWindow.setGravity(setGravity());
        dialogWindow.setAttributes(lp);
        try {
            initView();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initView() throws Exception;

    protected abstract void initData() throws Exception;

    public void setData(T t) {
        this.t = t;
    }

    public T getData() {
        return t;
    }

    public int setWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public int setHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onClick(View v) {

    }

    public int setGravity() {
        return Gravity.CENTER;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }
}