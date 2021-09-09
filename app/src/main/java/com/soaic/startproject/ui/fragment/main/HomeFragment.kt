package com.soaic.startproject.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.soaic.libcommon.weight.recyclerview.decoration.GridItemDecoration
import com.soaic.libcommon.weight.recyclerview.decoration.LinearItemDecoration
import com.soaic.startproject.databinding.HomeFragmentBinding
import com.soaic.startproject.mvp.presenter.TestPresenter
import com.soaic.startproject.mvp.presenter.impl.TestPresenterImpl
import com.soaic.startproject.mvp.view.ITestView
import com.soaic.startproject.response.TestResponse
import com.soaic.startproject.ui.adapter.home.TestAdapter
import com.soaic.startproject.ui.fragment.base.BasicFragment

class HomeFragment : BasicFragment(), ITestView {

    private lateinit var mBinding: HomeFragmentBinding
    private lateinit var testPresent: TestPresenter
    private lateinit var mAdapter: TestAdapter
    private var mData: MutableList<MutableList<String>> = mutableListOf()

    companion object {
        fun newInstance(): HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = HomeFragmentBinding.inflate(inflater, container, false)
        setRootView(mBinding)
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        testPresent = TestPresenterImpl(this)
        mBinding.testRecycleView.layoutManager = LinearLayoutManager(context)
        mBinding.testRecycleView.addItemDecoration(LinearItemDecoration.newBuilder().build())
        mAdapter = TestAdapter(context, mData)
        mBinding.testRecycleView.adapter = mAdapter
        testPresent.test("iphone")
    }

    override fun updateUI(test: TestResponse) {
        mData.addAll(test.result)
        mAdapter.notifyDataSetChanged()
    }


}