package com.soaic.startproject.mvp.model.impl

import com.soaic.startproject.mvp.model.TestModel
import com.soaic.startproject.response.TestResponse
import com.soaic.libcommon.network.HttpRequest
import com.soaic.libcommon.network.XUtilRequest
import com.soaic.libcommon.network.listener.HttpCallback

class TestModelImpl : TestModel {

    override fun test(str: String, callback: HttpCallback<TestResponse>) {
        HttpRequest.newBuilder(XUtilRequest.getInstance())
            .url("https://suggest.taobao.com/sug")
            .param("code", "utf-8")
            .param("q", str)
            .get()
            .build()
            .execute(TestResponse::class.java, callback)
    }

}