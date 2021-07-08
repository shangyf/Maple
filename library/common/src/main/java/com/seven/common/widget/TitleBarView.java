package com.seven.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.seven.basic.R;
import com.seven.common.utils.click.OnMultiClickListener;


/**
 * 标题栏
 * Created by SeVen on 2019-11-07 10:36
 */
public class TitleBarView extends LinearLayout {
    private LinearLayout ll_bg_title;
    private TextView tv_title;
    private TextView tv_right_title;
    private ImageView iv_back;

    /**
     * 左边的返回按钮
     */
    private int backArrow;

    /**
     * 背景
     */
    private int mainBackground;

    /**
     * 标题
     */
    private String title;

    /**
     * 标题字体大小
     */
    private int titleSize = 18;
    /**
     * 标题颜色
     */
    private int titleColor;

    /**
     * 右边的内容
     */
    private String rightTitle;
    private int rightColor;
    private int rightSize;

    private boolean isShowLeft;
    private boolean isShowRight;

    public TitleBarView(Context context) {
        this(context, null, 0);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        mainBackground = ta.getColor(R.styleable.TitleBarView_bgTitle, getResources().
                getColor(R.color.crimson));
        isShowLeft = ta.getBoolean(R.styleable.TitleBarView_isShowLeft, true);
        isShowRight = ta.getBoolean(R.styleable.TitleBarView_isShowRight, false);
        backArrow = ta.getResourceId(R.styleable.TitleBarView_backArrow, R.drawable.ic_arrow_back);
        title = ta.getString(R.styleable.TitleBarView_title);
        titleSize = ta.getInteger(R.styleable.TitleBarView_titleSize, 18);
        titleColor = ta.getColor(R.styleable.TitleBarView_titleColor, Color.WHITE);
        rightTitle = ta.getString(R.styleable.TitleBarView_rightTitle);
        rightColor = ta.getColor(R.styleable.TitleBarView_rightColor, Color.WHITE);
        rightSize = ta.getInteger(R.styleable.TitleBarView_rightSize, 14);


        LayoutInflater.from(context).inflate(R.layout.widget_title_bar, this);
        ll_bg_title = findViewById(R.id.ll_bg_title);
        iv_back = findViewById(R.id.btn_back);
        tv_title = findViewById(R.id.tv_title);
        tv_right_title = findViewById(R.id.tv_right);

        if (isShowLeft) {
            iv_back.setVisibility(View.VISIBLE);
            iv_back.setImageResource(backArrow);
            iv_back.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (onLeftClickListener != null) {
                        onLeftClickListener.onLeftClick(v);
                    } else {
                        ((Activity) context).finish();
                    }
                }
            });
        } else {
            iv_back.setVisibility(View.GONE);
        }

        ll_bg_title.setBackgroundColor(mainBackground);

        tv_title.setText(title);
        tv_title.setTextColor(titleColor);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, titleSize);

        if (isShowRight) {
            tv_right_title.setVisibility(View.VISIBLE);
            tv_right_title.setText(rightTitle);
            tv_right_title.setTextColor(rightColor);
            tv_right_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, rightSize);
            tv_right_title.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View view) {
                    if (onRightClickListener != null) {
                        onRightClickListener.onRightClick(view);
                    }
                }
            });
        } else {
            tv_right_title.setVisibility(View.GONE);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param resId
     */
    public void setTitleBackground(int resId) {
        ll_bg_title.setBackgroundColor(resId);
    }

    /**
     * 设置左边图片按钮
     *
     * @param resId
     */
    public void setLeftImage(int resId) {
        iv_back.setImageResource(resId);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置标题颜色
     *
     * @param resId
     */
    public void setTitleColor(int resId) {
        tv_title.setTextColor(resId);
    }

    /**
     * 设置标题大小
     *
     * @param size
     */
    public void setTitleSize(int size) {
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    /**
     * 设置右边标题
     *
     * @param rightTitle
     */
    public void setRightTitle(String rightTitle) {
        tv_right_title.setText(rightTitle);
    }

    /**
     * 设置右边字体颜色
     *
     * @param resId
     */
    public void setRightColor(int resId) {
        tv_right_title.setTextColor(resId);
    }

    /**
     * 设置右边字体大小
     *
     * @param size
     */
    public void setRightSize(int size) {
        tv_right_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    private OnRightClickListener onRightClickListener;
    private OnLeftClickListener onLeftClickListener;

    public void setOnRightClickListener(OnRightClickListener onRightClickListener) {
        this.onRightClickListener = onRightClickListener;
    }

    public void setOnLeftClickListener(OnLeftClickListener onLeftClickListener) {
        this.onLeftClickListener = onLeftClickListener;
    }

    public interface OnRightClickListener {
        void onRightClick(View view);
    }

    public interface OnLeftClickListener {
        void onLeftClick(View view);
    }
}
