package com.soaic.libcommon.network.response;

import com.soaic.libcommon.network.error.ServerError;
import com.soaic.libcommon.network.listener.HttpCallback;

public class BaseResponseHandler<T extends BaseResponse> {

    private HttpCallback<T> callback;

    public void setCallback(HttpCallback<T> callback) {
        this.callback = callback;
    }

    public void handleRsp(T rsp) {
        if (callback != null && rsp != null) {
            if (rsp.getCode() == 200) {
                callback.onSuccess(rsp);
            } else {
                callback.onFailure(new ServerError(rsp.getMsg()));
            }
        }
    }

}
