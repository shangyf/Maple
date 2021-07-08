package com.seven.common.frg;

import android.view.ViewStub;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seven.basic.R;

/**
 * Created by SeVen on 3/9/21 9:27 AM
 */
public abstract class BaseListFragment extends BaseFragment{
    public RecyclerView list_recycle;
    private ViewStub stub_view;

    //适配器
    public BaseQuickAdapter adapter;

    @Override
    protected void init() throws Exception {
        list_recycle = (RecyclerView) findViewById(R.id.list_recycle);
        list_recycle.setLayoutManager(getLayoutManager());
        list_recycle.setAdapter(createAdapter());
    }

    protected abstract BaseQuickAdapter createAdapter() throws Exception;

    protected abstract RecyclerView.LayoutManager getLayoutManager();

}