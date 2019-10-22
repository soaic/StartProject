package com.soaic.libcommon.network.response;

import androidx.annotation.NonNull;
import com.soaic.libcommon.proguard.IKeepFieldName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseResponse implements IKeepFieldName {

    private int code = 200;
    private String msg;
    private long serverTime;

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public long getRspTimeStamp() {
        return serverTime;
    }

    public void setRspTimeStamp(long serverTime) {
        this.serverTime = serverTime;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String toJsonWithExposeAnnotation() {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();
        return gson.toJson(this);
    }

    @NonNull
    @Override
    public String toString() {
        return toJson();
    }

}
