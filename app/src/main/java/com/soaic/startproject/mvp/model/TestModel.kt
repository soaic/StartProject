package com.soaic.startproject.mvp.model

import com.soaic.startproject.response.TestResponse
import com.soaic.libcommon.network.listener.HttpCallback

interface TestModel {

    fun test(str: String, callback: HttpCallback<TestResponse>)

}