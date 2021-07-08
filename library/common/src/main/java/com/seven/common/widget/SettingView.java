package com.seven.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seven.basic.R;
import com.seven.common.utils.click.OnMultiClickListener;


/**
 * 设置自定义按钮
 * Create by shangyf on 2019-10-14 15
 * What if I am the devil for you
 */
public class SettingView extends LinearLayout {
    private ImageView iv_leftImage;
    private TextView tv_title;
    private TextView tv_prompt;

    /**
     * 右边的图片
     */
    private int leftImageRes;

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

    private boolean isShowTopLine = false;
    private boolean isShowBottomLine = true;

    public SettingView(Context context) {
        this(context, null, 0);
    }

    public SettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }


    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingView);

        leftImageRes = ta.getResourceId(R.styleable.SettingView_leftImage, R.drawable.ic_map_24);

        title = ta.getString(R.styleable.SettingView_title);
        titleSize = ta.getInteger(R.styleable.SettingView_titleSize, 14);
        titleColor = ta.getColor(R.styleable.SettingView_titleColor, Color.parseColor("#333333"));

        rightTitle = ta.getString(R.styleable.SettingView_rightTitle);
        rightColor = ta.getColor(R.styleable.SettingView_rightColor, Color.parseColor("#999999"));
        rightSize = ta.getInteger(R.styleable.SettingView_rightSize, 12);

        isShowTopLine = ta.getBoolean(R.styleable.SettingView_isShowTopLine, false);
        isShowBottomLine = ta.getBoolean(R.styleable.SettingView_isShowBottomLine, true);

        LayoutInflater.from(context).inflate(R.layout.widget_setting_view, this);
        iv_leftImage = findViewById(R.id.left_image);
        tv_title = findViewById(R.id.tv_title);
        tv_prompt = findViewById(R.id.tv_prompt);
        findViewById(R.id.top_line).setVisibility(isShowTopLine ? View.VISIBLE : View.GONE);
        findViewById(R.id.bottom_line).setVisibility(isShowBottomLine ? View.VISIBLE : View.GONE);
        findViewById(R.id.ll_setting_view).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (onSettingClickListener != null) {
                    onSettingClickListener.onSettingClick(view);
                }
            }
        });

        iv_leftImage.setImageResource(leftImageRes);

        tv_title.setText(title);
        tv_title.setTextColor(titleColor);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, titleSize);

        tv_prompt.setText(rightTitle);
        tv_prompt.setTextColor(rightColor);
        tv_prompt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, rightSize);

    }

    /**
     * 设置左边图片按钮
     *
     * @param resId
     */
    public void setLeftImage(int resId) {
        iv_leftImage.setImageResource(resId);
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
        tv_prompt.setText(rightTitle);
    }

    /**
     * 设置右边字体颜色
     *
     * @param resId
     */
    public void setRightColor(int resId) {
        tv_prompt.setTextColor(resId);
    }

    /**
     * 设置右边字体大小
     *
     * @param size
     */
    public void setRightSize(int size) {
        tv_prompt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }


    private OnSettingClickListener onSettingClickListener;

    public void setOnSettingClickListener(OnSettingClickListener onSettingClickListener) {
        this.onSettingClickListener = onSettingClickListener;
    }

    public interface OnSettingClickListener {
        void onSettingClick(View view);
    }

}
