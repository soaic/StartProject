package com.soaic.libcommon.network;

import com.soaic.libcommon.network.listener.ProgressListener;

import java.io.File;

public class RequestFile {

    public String key;
    public File file;
    public ProgressListener progressListener;   //文件上传进度监听

    public static RequestFile create(String key, File file, ProgressListener progressListener){
        RequestFile requestFile = new RequestFile();
        requestFile.file = file;
        requestFile.key = key;
        requestFile.progressListener = progressListener;
        return requestFile;
    }

    public static RequestFile create(String key, File file) {
        RequestFile requestFile = new RequestFile();
        requestFile.file = file;
        requestFile.key = key;
        return requestFile;
    }

}
