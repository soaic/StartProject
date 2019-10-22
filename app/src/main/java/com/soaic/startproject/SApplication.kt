package com.soaic.startproject

import android.app.Application
import com.soaic.libcommon.AppEnv
import org.xutils.x

class SApplication: Application() {

    //静态变量与方法块
    companion object {
        private lateinit var app: Application
        fun getApplication () : Application {
            return app
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        x.Ext.init(app)
        AppEnv.init(app)
    }
}