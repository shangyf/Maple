package com.seven.net.callback;

import com.seven.net.bean.ResponseBean;

/**
 * Created by SeVen on 2019-11-12 14:34
 */
public interface CallBack<T> {
    void onResponse(ResponseBean<T> responseBean);

    void onFailure(Throwable t);
}
