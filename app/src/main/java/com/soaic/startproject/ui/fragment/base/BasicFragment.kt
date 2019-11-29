package com.soaic.startproject.ui.fragment.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.soaic.startproject.R
import com.soaic.libcommon.utils.ToastUtils


abstract class BasicFragment : Fragment() {
    private var loadingDialog: Dialog? = null
    private lateinit var mViewGroup: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return mViewGroup
    }

    protected fun setRootView(binding: ViewDataBinding) {
        setRootView(binding.root as ViewGroup)
    }

    protected fun setRootView(rootView: ViewGroup) {
        mViewGroup = rootView
        mViewGroup.setOnTouchListener { _, _ -> true }
        mViewGroup.id = R.id.fragment_root
    }

    fun getRootView(): ViewGroup {
        return mViewGroup
    }


    /** 显示加载对话框  */
    fun showLoading() {
        if (loadingDialog == null && activity != null) {
            loadingDialog = Dialog(activity!!, R.style.customDialogStyle)
            loadingDialog!!.setContentView(R.layout.dialog_custom_loading)
            loadingDialog!!.setCanceledOnTouchOutside(false)
            loadingDialog!!.setCancelable(true)
        }
        if (activity != null && !loadingDialog!!.isShowing)
            loadingDialog!!.show()
    }

    /** 隐藏加载对话框  */
    fun hideLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
    }

    fun startActivity(@NonNull openClass: Class<*>) {
        val intent = Intent(context, openClass)
        startActivity(intent)
    }

    fun startActivity(@NonNull openClass: Class<*>, @NonNull bundle: Bundle) {
        val intent = Intent(context, openClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun startActivityForResult(@NonNull openClass: Class<*>, @NonNull bundle: Bundle, requestCode: Int) {
        val intent = Intent(context, openClass)
        intent.putExtras(bundle)
        startActivityForResult(intent, requestCode)
    }

    fun showToast(str: String) {
        ToastUtils.showShortToast(context, str)
    }

    fun showToast(@IdRes rid: Int) {
        ToastUtils.showShortToast(context, rid)
    }

}