package com.seven.maple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by SeVen on 3/11/21 2:51 PM
 */
public class DView extends View {
    Paint mPaint = new Paint();

    public DView(Context context) {
        super(context);
    }

    public DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.lineTo(0, 0);
        // path.moveTo(0,100);
        path.lineTo(100, 0);
        path.lineTo(100, 100);
        path.moveTo(100, 100);
        path.lineTo(100, 0);
//        RectF rectF = new RectF(200,200,600,600);
//        path.addArc(rectF,0,120);
        mPaint.setTextSize(50);
        mPaint.setStyle(Paint.Style.FILL);
        // 绘制路径
        canvas.drawPath(path, mPaint);
        mPaint.setColor(Color.BLUE);
        String text = "12";
        canvas.drawTextOnPath(text, path, 0f, 0f, mPaint);
    }
}