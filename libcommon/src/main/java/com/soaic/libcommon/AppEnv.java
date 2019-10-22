package com.soaic.libcommon;

import android.content.Context;

public class AppEnv {

    private static Context mAppContext;

    public static void init(Context context){
        mAppContext = context;
    }

    public static Context getApplicationContext() {
        checkEnv();
        return mAppContext;
    }

    private static void checkEnv() {
        if (mAppContext == null) {
            throw new RuntimeException("need invoke AppEnv.init() in your Application.onCrate()");
        }
    }
}
