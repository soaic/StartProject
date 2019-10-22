package com.soaic.startproject.ui.activity.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.soaic.startproject.ui.activity.MainActivity
import com.soaic.startproject.R
import com.soaic.libcommon.utils.AndroidBug5497Workaround
import com.soaic.libcommon.utils.KeyboardUtils
import com.soaic.libcommon.utils.StatusBarCompat
import com.soaic.libcommon.utils.ToastUtils

abstract class BasicActivity : AppCompatActivity(), AndroidBug5497Workaround.OnKeyboardVisibleChangedListener {

    private var loadingDialog: Dialog? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if ((intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish()
            return
        }

        if (savedInstanceState != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        AndroidBug5497Workaround.assistActivity(this, this)
        StatusBarCompat.translucentStatusBar(this)
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        AndroidBug5497Workaround.assistActivity(this, this)
        StatusBarCompat.translucentStatusBar(this)
    }

    /** 显示加载对话框  */
    fun showProgressDialog() {
        if (loadingDialog == null) {
            loadingDialog = Dialog(this, R.style.customDialogStyle)
        }
        loadingDialog!!.setContentView(R.layout.dialog_custom_loading)
        loadingDialog!!.setCanceledOnTouchOutside(false)
        loadingDialog!!.setCancelable(true)

        if (!loadingDialog!!.isShowing)
            loadingDialog!!.show()
    }

    fun showProgressDialog(isDim: Boolean) {
        showProgressDialog()
        if (isDim) {
            loadingDialog?.window?.setDimAmount(0f)
        }
    }

    /** 隐藏加载对话框  */
    fun hideProgressDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
    }

    fun startActivity(openClass: Class<*>) {
        val intent = Intent(this, openClass)
        startActivity(intent)
    }

    fun startActivity(openClass: Class<*>, bundle: Bundle) {
        val intent = Intent(this, openClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun startActivityForResult(openClass: Class<*>, bundle: Bundle, requestCode: Int) {
        val intent = Intent(this, openClass)
        intent.putExtras(bundle)
        startActivityForResult(intent, requestCode)
    }

    fun showToast(str: String) {
        ToastUtils.showShortToast(this, str)
    }

    fun showToast(@StringRes rid: Int) {
        ToastUtils.showShortToast(this, rid)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            //点击空白处隐藏软键盘
            val v = currentFocus
            if (KeyboardUtils.isShouldHideKeyboard(v, ev)) {
                KeyboardUtils.hideSoftInput(this)
                return super.dispatchTouchEvent(ev)
            }
        }
        return if (ev.pointerCount > 0 && window.superDispatchTouchEvent(ev)) true else onTouchEvent(ev)
    }

    protected fun getActivity(): BasicActivity {
        return this
    }

    @CallSuper
    override fun onKeyboardVisibleChanged(visible: Boolean, contentSizeChanged: Boolean, keyboardHeight: Int) {

    }
}