package com.seven.common.utils.click;

import android.view.View;

/**
 * 防止按钮多次点击
 * Create by shangyf on 2019-10-14 15
 * What if I am the devil for you
 */
public abstract class OnMultiClickListener implements View.OnClickListener {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    //最后点击的时间
    private static long lastClickTime;

    public abstract void onMultiClick(View view);

    @Override
    public void onClick(View view) {
        //当前点击按钮的时间
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(view);
        }
    }
}
