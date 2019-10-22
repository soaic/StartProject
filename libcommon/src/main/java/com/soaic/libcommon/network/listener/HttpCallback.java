package com.soaic.libcommon.network.listener;

import com.soaic.libcommon.network.response.BaseResponse;

/**
 * 网络请求监听
 * Created by XiaoSai on 2016/11/3.
 */
public interface HttpCallback<T extends BaseResponse> {

    /** 请求成功*/
    void onSuccess(T t);

    /** 请求失败 根据需求实现*/
    void onFailure(Throwable err);

}
