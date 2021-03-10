package com.seven.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SeVen on 2020/3/24 11:25
 */
public abstract  class BaseListAdapter<E>   extends BaseAdapter {
    private List<E> baseList = new ArrayList<E>();

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected Intent intent;

    public BaseListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public BaseListAdapter(Context mContext, List<E> baseList) {
        this(mContext);
        mInflater = LayoutInflater.from(mContext);
        this.baseList = baseList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return baseList == null ? 0 : baseList.size();
    }

    @Override
    public E getItem(int position) {
        return baseList.get(position);
    }

    /**
     * 清空数据
     */
    public void clearAll() {
        baseList.clear();
    }

    /**
     * 获取当前数据
     *
     * @return
     */
    public List<E> getData() {
        return baseList;
    }

    /**
     * @param lists
     */
    public void addALL(List<E> lists) {
        if (lists == null || lists.size() == 0) {
            return;
        }
        baseList.addAll(lists);
    }

    /**
     * 添加单个对象
     *
     * @param item
     */
    public void add(E item) {
        baseList.add(item);
    }

    /**
     * 移除单个对象
     *
     * @param e
     */
    public void removeEntity(E e) {
        baseList.remove(e);
    }

}