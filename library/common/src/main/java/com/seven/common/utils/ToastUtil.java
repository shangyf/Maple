package com.seven.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seven.basic.R;


/**
 * Created by Samuel.shang on 2017/10/27.
 */

public class ToastUtil {
    private static Toast mToast = null;

    public enum ToastType {
        SUCCESS, FAILURE, NONE
    }

    public static void showToast(Context context, String msg, ToastType type) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_toast, null);
        LinearLayout toast_layout = view.findViewById(R.id.toast_layout);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(
                DensityUtil.getWindowWidth(context) - DensityUtil.dip2px(context, 20),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        toast_layout.setLayoutParams(vp);
        ImageView iv_prompt = view.findViewById(R.id.iv_prompt);

        switch (type) {
            case SUCCESS:
                iv_prompt.setImageResource(R.mipmap.common_bounced_icon_successful);
                break;
            case FAILURE:
                iv_prompt.setImageResource(R.mipmap.common_bounced_icon_warning);
                break;
            case NONE:
                iv_prompt.setVisibility(View.GONE);
                break;
        }

        TextView toast_test = view.findViewById(R.id.toast_test);
        toast_test.setText(msg);
        if (mToast == null) {
            mToast = new Toast(context);
            mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                    0, DensityUtil.dip2px(context, 5));
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(view);
        } else {
            mToast.setView(view);
        }
        mToast.show();
    }


    /**
     * @param context
     * @param msg
     * @param type
     */
    public static void showToast(Context context, int msg, ToastType type) {
        showToast(context, context.getResources().getString(msg), type);
    }

    /**
     * 取消提示框
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
