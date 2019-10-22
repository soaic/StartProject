package com.soaic.libcommon.network;

import androidx.annotation.NonNull;

import com.soaic.libcommon.network.listener.HttpCallback;
import com.soaic.libcommon.network.response.BaseResponse;

public abstract class HttpRequest {

    abstract void initData(Builder builder);

    public abstract <T extends BaseResponse> void execute(@NonNull final Class<T> clazz, final HttpCallback<T> callback);

    public static Builder newBuilder(@NonNull HttpRequest request) {
        return new Builder(request);
    }

}
