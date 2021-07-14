package com.seven.common.frg;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.seven.basic.R;

import java.util.List;

/**
 * Created by SeVen on 3/9/21 9:27 AM
 */
public abstract class BaseListFragment extends BaseFragment {
    public RecyclerView list_recycle;
    private ViewStub stub_view;
    //刷新
    protected SmartRefreshLayout refreshLayout;

    //适配器
    public BaseQuickAdapter adapter;

    //分页
    public final int pagerSize = 20;
    public int pagerIndex = 1;


    private boolean isRefresh = false;
    private boolean isLoadMore = false;


    @Override
    protected void init() throws Exception {
        list_recycle = (RecyclerView) findViewById(R.id.list_recycle);
        list_recycle.setLayoutManager(getLayoutManager());
        stub_view = (ViewStub) findViewById(R.id.stub_view);
        list_recycle.setLayoutManager(getLayoutManager());
        list_recycle.setAdapter(createAdapter());

        if (enableRefreshLayout()) {
            refreshLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
            refreshLayout.setEnableRefresh(enableRefresh());
            refreshLayout.setEnableLoadMore(enableLoadMore());
            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    isLoadMore = true;
                    request();
                }

                @Override
                public void onRefresh(RefreshLayout refreshLayout) {
                    isRefresh = true;
                    BaseListFragment.this.onRefresh();
                }
            });
        }

        showErrorView(-1, errorMsg(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
    }

    protected abstract BaseQuickAdapter createAdapter() throws Exception;

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    /**
     * 是否添加刷新的layout
     *
     * @return
     */
    public boolean enableRefreshLayout() {
        return false;
    }

    /**
     * 是否刷新
     *
     * @return
     */
    public boolean enableRefresh() {
        return true;
    }

    /**
     * 是否加载更多
     *
     * @return
     */
    public boolean enableLoadMore() {
        return false;
    }

    /**
     * 显示错误信息
     *
     * @return
     */
    public String errorMsg() {
        return "--暂无可显示数据--";
    }

    @Override
    public void onRefresh() {
        //初始化
        pagerIndex = 1;
        super.onRefresh();
    }


    /**
     * 显示
     *
     * @param resId
     * @param errorMsg
     */
    public void showErrorView(int resId, String errorMsg, View.OnClickListener listener) {
        if (stub_view != null) {
            if (enableRefreshLayout()) {
                refreshLayout.setVisibility(View.GONE);
            } else {
                if (list_recycle != null) {
                    list_recycle.setVisibility(View.GONE);
                }
            }
            if (stub_view.getParent() != null) {
                stub_view.inflate();
            } else {
                stub_view.setVisibility(View.VISIBLE);
            }
            ((TextView) findViewById(R.id.tv_error)).setText(errorMsg);
            ImageView tv_error_image = (ImageView) findViewById(R.id.iv_error);
            tv_error_image.setOnClickListener(listener);
            if (resId != -1) {
                tv_error_image.setImageResource(resId);
            }
        }
    }


    public void goneErrorView() {
        if (stub_view != null) {
            if (enableRefreshLayout()) {
                refreshLayout.setVisibility(View.VISIBLE);
            } else {
                if (list_recycle != null) {
                    list_recycle.setVisibility(View.VISIBLE);
                }
            }
            stub_view.setVisibility(View.GONE);
        }
    }

    /**
     * 加载成功之后更新UI
     *
     * @param list
     * @param <T>
     */
    public <T> void onSuccess(List<T> list) {
        super.onSuccess();
        if (pagerIndex == 1) {
            if (list == null || list.size() == 0) {
                //删除列表中的旧数据，比如第一次第一页访问返回3条，第二次第一页访问返回0条
                if (adapter.getData() != null && adapter.getData().size() > 0) {
                    adapter.getData().clear();
                }
                showErrorView(-1, errorMsg(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRefresh();
                    }
                });
            } else {
                goneErrorView();
                adapter.setNewData(list);
                if (enableRefreshLayout()) {
                    pagerIndex++;
                }
            }
        } else {
            if (list != null && list.size() > 0) {
                adapter.addData(list);
                pagerIndex++;
            }
        }
        adapter.notifyDataSetChanged();
        if (refreshLayout != null && isRefresh) {
            refreshLayout.finishRefresh();
            isRefresh = false;
        }

        if (refreshLayout != null && isLoadMore) {
            refreshLayout.finishLoadMore();
            isLoadMore = false;
        }
    }


    /**
     * 失败之后处理一下
     *
     * @param errorMsg
     */
    public void onFailure(String errorMsg) {
        super.onFailure(errorMsg);
        //适配器处理失败之后的数据
        if (pagerIndex == 1) {
            showErrorView(-1, errorMsg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh();
                }
            });
        } else { //其他页失败之后显示前面的数据就行
            goneErrorView();
            if (!TextUtils.isEmpty(errorMsg)) {
                showToastFailure(errorMsg);
            }
        }
        if (refreshLayout != null && isRefresh) {
            refreshLayout.finishRefresh();
            isRefresh = false;
        }

        if (refreshLayout != null && isLoadMore) {
            refreshLayout.finishLoadMore();
            isLoadMore = false;
        }
    }

}