package com.soaic.libcommon.network;

import androidx.annotation.NonNull;

import com.soaic.libcommon.network.listener.HttpCallback;
import com.soaic.libcommon.network.response.BaseResponse;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class XUtilRequest extends HttpRequest {

    private RequestParams params;
    private Builder mBuild;
    private Gson gson = new Gson();

    public static XUtilRequest getInstance() {
        return SingleLoader.INSTANCE;
    }

    private static class SingleLoader {
        private final static XUtilRequest INSTANCE = new XUtilRequest();
    }

    @Override
    void initData(Builder builder) {
        this.mBuild = builder;

        params = new RequestParams(builder.url);

        for (String key : builder.params.keySet()) {
            if (builder.method.equals(Builder.GET)) {
                params.addQueryStringParameter(key, builder.params.get(key));
            } else {
                params.addBodyParameter(key, builder.params.get(key));
            }
        }

        for (String key : builder.bodyParams.keySet()) {
            params.addBodyParameter(key, builder.bodyParams.get(key), "application/octet-stream");
        }

        for (String key: mBuild.headers.keySet()) {
            params.addHeader(key, mBuild.headers.get(key));
        }
    }

    @Override
    public <T extends BaseResponse> void execute(@NonNull final Class<T> clazz, final HttpCallback<T> callback) {

        HttpMethod method = mBuild.method.equals(Builder.GET) ? HttpMethod.GET : HttpMethod.POST;

        x.http().request(method, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (callback != null) {
                    T rsp = gson.fromJson(result, clazz);
                    callback.onSuccess(rsp);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                if (callback != null)
                    callback.onFailure(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                cex.printStackTrace();
                if (callback != null)
                    callback.onFailure(cex);
            }

            @Override
            public void onFinished() {}
        });

    }
}
