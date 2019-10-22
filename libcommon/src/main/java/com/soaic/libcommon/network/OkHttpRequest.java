package com.soaic.libcommon.network;

import androidx.annotation.NonNull;

import com.soaic.libcommon.AppEnv;
import com.soaic.libcommon.network.error.ParseError;
import com.soaic.libcommon.network.error.ServerError;
import com.soaic.libcommon.network.https.OkHttpSSLSocketFactory;
import com.soaic.libcommon.network.interceptor.HttpLoggingInterceptor;
import com.soaic.libcommon.network.listener.HttpCallback;
import com.soaic.libcommon.network.listener.ProgressListener;
import com.soaic.libcommon.network.requestbody.CustomRequestBody;
import com.soaic.libcommon.network.response.BaseResponse;
import com.soaic.libcommon.utils.HandlerUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp 网络请求
 * created by sxiao by 19/4/12
 */
public class OkHttpRequest extends HttpRequest {
    private OkHttpClient mClient;
    private Request mRequest;
    private Builder mBuilder;
    private Gson gson = new Gson();

    public static OkHttpRequest getInstance() {
        return SingleLoader.INSTANCE;
    }

    private static class SingleLoader {
        private final static OkHttpRequest INSTANCE = new OkHttpRequest();
    }

    private OkHttpRequest() {
        mClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .sslSocketFactory(OkHttpSSLSocketFactory.getSocketFactory(), OkHttpSSLSocketFactory.getTrustManager()) //设置SSL
                .hostnameVerifier(OkHttpSSLSocketFactory.getHostnameVerifier())
                .cache(new Cache(AppEnv.getApplicationContext().getCacheDir(),10 * 1024 * 1024))   //设置缓存目录和10M缓存
                .build();
    }

    @Override
    public void initData(Builder builder) {
        this.mBuilder = builder;
        if (Builder.POST.equals(mBuilder.method)) {
            initPost();
        } else {
            initGet();
        }
    }

    // 初始化GET请求
    private void initGet() {
        StringBuilder paramStr = new StringBuilder();
        for (String key : mBuilder.params.keySet()) {
            paramStr.append(key).append("=").append(mBuilder.params.get(key)).append("&");
        }
        if (paramStr.length() > 0) {
            paramStr = paramStr.deleteCharAt(paramStr.length() - 1);
            mBuilder.url = mBuilder.url + "?" + paramStr;
        }
        mRequest = new Request.Builder()
                .headers(Headers.of(mBuilder.headers))
                .url(mBuilder.url)
                .get()
                .build();
    }

    // 初始化POST请求
    private void initPost() {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        //设置为表单类型
        bodyBuilder.setType(MultipartBody.FORM);
        //添加表单键值
        for (String key : mBuilder.params.keySet()) {
            String value = mBuilder.params.get(key);
            if (value != null)
                bodyBuilder.addFormDataPart(key, value);
        }
        for (String key : mBuilder.bodyParams.keySet()) {
            final RequestFile requestFile = mBuilder.bodyParams.get(key);
            if (requestFile != null) {
                RequestBody value;
                if (requestFile.progressListener == null) {
                    value = RequestBody.create(MediaType.parse("application/octet-stream"), requestFile.file);
                } else {
                    value = CustomRequestBody.create(MediaType.parse("application/octet-stream"), requestFile.file, new ProgressListener() {
                        @Override
                        public void onProgress(float num) {
                            final float progress = (num / (float) requestFile.file.length()) * 100;
                            HandlerUtil.postOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestFile.progressListener != null) {
                                        requestFile.progressListener.onProgress(progress);
                                    }
                                }
                            });
                        }
                    });
                }
                bodyBuilder.addFormDataPart(requestFile.key, requestFile.file.getName(), value);
            }
        }
        mRequest = new Request.Builder()
                .headers(Headers.of(mBuilder.headers))
                .url(mBuilder.url)
                .post(bodyBuilder.build())
                .build();
    }

    /**
     * 执行异步请求
     */
    @Override
    public <T extends BaseResponse> void execute(@NonNull final Class<T> clazz, final HttpCallback<T> callback) {
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    //需要在异步处理
                    ResponseBody body = response.body();
                    if (body == null) {
                        if (callback != null) {
                            callback.onFailure(new ServerError("body is null"));
                        }
                        return;
                    }
                    final String content = body.string();
                    HandlerUtil.postOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                T rsp = gson.fromJson(content, clazz);
                                if (callback != null) {
                                    boolean isError = false;
                                    if (mBuilder.serverErrorInterceptor != null) {
                                        isError = mBuilder.serverErrorInterceptor.isServerError(rsp);
                                    }
                                    if (!isError) {
                                        callback.onSuccess(rsp);
                                    } else if (mBuilder.serverErrorInterceptor.getServerError() != null) {
                                        callback.onFailure(mBuilder.serverErrorInterceptor.getServerError());
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                if (callback != null) {
                                    callback.onFailure(new ParseError(e));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (callback != null) {
                                    callback.onFailure(new ServerError(e));
                                }
                            }
                        }
                    });
                } catch (final IOException e) {
                    if (callback != null) {
                        HandlerUtil.postOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(new ParseError(e));
                            }
                        });
                    }
                }
            }
        });
    }
}
