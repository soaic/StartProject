package com.soaic.startproject.response

import com.soaic.libcommon.network.response.BaseResponse

class TestResponse: BaseResponse() {
    var result: MutableList<MutableList<String>> = mutableListOf()
    var tmall: String = ""
}