package com.soaic.startproject.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soaic.startproject.databinding.MoreFragmentBinding
import com.soaic.startproject.ui.fragment.base.BasicFragment

class MoreFragment : BasicFragment() {

    private lateinit var mBinding: MoreFragmentBinding

    companion object {
        fun newInstance(): MoreFragment{
            return MoreFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = MoreFragmentBinding.inflate(inflater, null, false)
        setRootView(mBinding)
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {


    }


}