package com.seven.common.act;

import android.view.ViewStub;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by SeVen on 3/9/21 9:27 AM
 */
public abstract class BaseListActivity extends BaseActivity{
    private ViewStub stub_view;

    //适配器
    public BaseQuickAdapter adapter;

    protected abstract BaseQuickAdapter createAdapter() throws Exception;

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    @Override
    protected void init() throws Throwable {

    }
}