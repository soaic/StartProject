package com.soaic.startproject.mvp.view

import com.soaic.startproject.response.TestResponse

interface ITestView {
    fun updateUI(test: TestResponse)
}