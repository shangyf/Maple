package com.seven.common.frg;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by SeVen on 3/8/21 9:58 AM
 */
public abstract class BaseFragment extends Fragment {
    protected static String TAG = BaseFragment.class.getSimpleName();

    public View contentView = null;

    protected LayoutInflater mInflater;
    /**
     * 加载进度条
     */
    private KProgressHUD kProgressHUD;

    protected Bundle savedInstanceState;

    protected Context mContext;


    //获取布局文件ID
    protected abstract int getLayoutId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == contentView) {
            contentView = onCreateDataBinding(inflater, container, savedInstanceState);
            if (null == contentView) {
                contentView = inflater.inflate(getLayoutId(), container, false);
            }
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (parent != null) {
            parent.removeView(contentView);
        }
        this.savedInstanceState = savedInstanceState;
        return contentView;
    }

    public View onCreateDataBinding(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mInflater = LayoutInflater.from(mContext);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化view
     */
    protected abstract void init() throws Exception;


    public View findViewById(int resId) {
        if (contentView == null) {
            throw new RuntimeException("fragment view is null");
        }
        return contentView.findViewById(resId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Bundle arguments = getArguments();
        if (arguments != null) {
            handlerArguments(arguments);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    /**
     * 处理从Activity传过来的参数
     *
     * @param arguments
     */
    protected void handlerArguments(Bundle arguments) {
    }

    /**
     * 显示加载进度条
     */
    public void showLoading(String msg) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(getActivity());
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