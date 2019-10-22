package com.soaic.startproject.mvp.presenter.impl

import com.soaic.startproject.mvp.model.TestModel
import com.soaic.startproject.mvp.model.impl.TestModelImpl
import com.soaic.startproject.mvp.presenter.TestPresenter
import com.soaic.startproject.mvp.view.ITestView
import com.soaic.startproject.response.TestResponse
import com.soaic.libcommon.network.listener.HttpCallback

class TestPresenterImpl(private var mView: ITestView) : TestPresenter {

    private var mTestModel: TestModel = TestModelImpl()

    override fun test(str: String) {
        mTestModel.test(str, object: HttpCallback<TestResponse> {
            override fun onSuccess(t: TestResponse) {
                mView.updateUI(t)
            }

            override fun onFailure(err: Throwable) {

            }
        })
    }
}