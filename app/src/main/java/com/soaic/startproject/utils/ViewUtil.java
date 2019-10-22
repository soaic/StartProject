package com.soaic.startproject.utils;

import android.view.View;

import com.soaic.startproject.SApplication;
import com.soaic.startproject.R;
import com.soaic.startproject.base.AppConstants;
import com.soaic.libcommon.utils.StatusBarCompat;

public class ViewUtil {

    public static void setVisible(View view, boolean visible) {
        if(view == null)
            return;
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void setInVisible(View view, boolean inVisible) {
        if(view == null)
            return;
        if (inVisible) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static boolean isVisible(View view) {
        if(view == null)
            return false;
        return view.getVisibility() == View.VISIBLE;
    }

    public static int getTitlebarHeight() {
        int statusBarHeight = StatusBarCompat.getStatusBarHeight(SApplication.Companion.getApplication());
        return SApplication.Companion.getApplication().getResources().getDimensionPixelOffset(R.dimen.titlebar_height) + statusBarHeight;
    }

    public static int getTitlebarBackground() {
        if (AppConstants.IsReleaseModel) {
            return R.drawable.titlebar_bg;
        } else {
            String svr = AppUtils.getServerAddress();
            if (AppConstants.Value.DEV_SERVER.equals(svr)) {
                return R.drawable.titlebar_bg_dev;
            } else if (AppConstants.Value.QA_SERVER.equals(svr)) {
                return R.drawable.titlebar_bg_qa;
            } else if (AppConstants.Value.RELEASE_SERVER.equals(svr)) {
                return R.drawable.titlebar_bg;
            } else if(AppConstants.Value.STAG_SERVER.equals(svr)){
                return R.drawable.titlebar_bg_stag;
            }
            return R.drawable.titlebar_bg_transparent;
        }
    }

}
