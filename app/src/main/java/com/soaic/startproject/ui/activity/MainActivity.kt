package com.soaic.startproject.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.soaic.startproject.R
import com.soaic.startproject.base.AppConstants
import com.soaic.startproject.databinding.MainActivityBinding
import com.soaic.startproject.ui.activity.base.BasicActivity
import com.soaic.startproject.ui.fragment.base.BasicFragment
import com.soaic.startproject.ui.fragment.main.HomeFragment
import com.soaic.startproject.ui.fragment.main.MoreFragment
import com.soaic.startproject.weight.DockerBar
import com.soaic.libcommon.base.BasicFragmentPagerAdapter

class MainActivity : BasicActivity() {

    private lateinit var mBinding: MainActivityBinding
    private lateinit var mAdapter: BasicFragmentPagerAdapter<BasicFragment>
    private lateinit var mFragmentList: MutableList<BasicFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        initView()
    }

    private fun initView() {
        mFragmentList = mutableListOf()
        mFragmentList.add(HomeFragment.newInstance())
        mFragmentList.add(HomeFragment.newInstance())
        mFragmentList.add(HomeFragment.newInstance())
        mFragmentList.add(HomeFragment.newInstance())
        mFragmentList.add(MoreFragment.newInstance())
        mAdapter = BasicFragmentPagerAdapter(supportFragmentManager, mFragmentList)
        mBinding.viewPager.offscreenPageLimit = 5
        mBinding.viewPager.adapter = mAdapter
        mBinding.viewPager.setNoScroll(true)
        mBinding.viewPager.addOnPageChangeListener(mOnPageChangeListener)
        mBinding.dockerBar.setOnItemClickListener(mOnItemClickOfDockerBar)
    }

    private val mOnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }
        override fun onPageSelected(position: Int) {
            mBinding.dockerBar.setSelection(position)
        }
        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    private val mOnItemClickOfDockerBar = DockerBar.OnItemClickListener { _, item -> switchPage(item.pageId) }

    private fun switchPage(pageId: Int) {
        when (pageId) {
            AppConstants.PageId.Exit -> finish()
            AppConstants.PageId.Home -> {
                mBinding.viewPager.setCurrentItem(0, false)
            }
            AppConstants.PageId.Classify -> {
                mBinding.viewPager.setCurrentItem(1, false)
            }
            AppConstants.PageId.Deals -> {
                mBinding.viewPager.setCurrentItem(2, false)
            }
            AppConstants.PageId.Cart -> {
                mBinding.viewPager.setCurrentItem(3, false)
            }
            AppConstants.PageId.Personal -> {
                mBinding.viewPager.setCurrentItem(4, false)
            }
        }
    }
}
