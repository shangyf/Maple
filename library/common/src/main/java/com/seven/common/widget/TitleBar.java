package com.seven.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.seven.basic.R;

/**
 * 标题
 * Created by SeVen on 3/9/21 2:40 PM
 */
public class TitleBar extends LinearLayout {
    private TextView tv_title;

    /**
     * 标题
     */
    private String title;
    /**
     * 右边的内容
     */
    private String rightTitle;

    public TitleBar(Context context) {
        this(context,null,0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context,attrs);
    }

    public void init(Context context,@Nullable AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.widget_title_bar,this);
    }
}