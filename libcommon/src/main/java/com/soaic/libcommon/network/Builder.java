package com.soaic.libcommon.network;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.soaic.libcommon.network.interceptor.ServerErrorInterceptor;
import com.soaic.libcommon.network.listener.ProgressListener;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Builder {
    String url = "";
    LinkedHashMap<String, String> params = new LinkedHashMap<>(); //LinkedHashMap顺序排列
    LinkedHashMap<String, RequestFile> bodyParams = new LinkedHashMap<>();
    LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    HttpRequest mRequest;
    String method = "get";
    static String GET = "get";
    static String POST = "post";
    ServerErrorInterceptor serverErrorInterceptor;

    Builder(HttpRequest request) {
        this.mRequest = request;
    }

    public Builder get() {
        this.method = GET;
        return this;
    }

    public Builder post() {
        this.method = POST;
        return this;
    }

    public Builder url(String url) {
        this.url = url;
        return this;
    }

    public Builder param(String key, String value) {
        if (key != null && value != null) {
            params.put(key, value);
        }
        return this;
    }

    public Builder params(Map<String, String> maps) {
        if (maps != null) {
            for (String key : maps.keySet()) {
                if (key != null && maps.get(key) != null) {
                    params.put(key, maps.get(key));
                }
            }
        }
        return this;
    }

    public Builder header(String key, String value) {
        if (key != null && value != null) {
            headers.put(key, value);
        }
        return this;
    }

    public Builder headers(@NonNull Map<String, String> maps) {
        for (String key : maps.keySet()) {
            if (key != null && maps.get(key) != null) {
                headers.put(key, maps.get(key));
            }
        }
        return this;
    }

    public Builder files(@NonNull Map<String, File> mapFiles) {
        for (String key : mapFiles.keySet()) {
            if (key != null && mapFiles.get(key) != null) {
                bodyParams.put(key, RequestFile.create(key, mapFiles.get(key)));
            }
        }
        return this;
    }

    public Builder file(String key, File file) {
        if (key != null && file != null) {
            bodyParams.put(key, RequestFile.create(key, file));
        }
        return this;
    }

    public Builder file(String key, final File file, final ProgressListener listener) {
        if (key != null && file != null) {
            bodyParams.put(key, RequestFile.create(key, file, listener));
        }
        return this;
    }

    public Builder serverErrorInterceptor(ServerErrorInterceptor serverErrorInterceptor) {
        this.serverErrorInterceptor = serverErrorInterceptor;
        return this;
    }

    public HttpRequest build() {
        if (mRequest == null) {
            throw new NullPointerException("HttpRequest can't null!");
        }
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("Url can't null!");
        }
        mRequest.initData(this);
        return mRequest;
    }
}
