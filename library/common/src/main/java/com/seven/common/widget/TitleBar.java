package com.seven.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 标题
 * Created by SeVen on 3/9/21 2:40 PM
 */
public class TitleBar extends LinearLayout {
    public TitleBar(Context context) {
        this(context,null,0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}