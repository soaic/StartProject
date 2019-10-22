package com.soaic.libcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by ifels on 2017/11/3.
 */

public class AndroidBug5497Workaround {
    private static final String TAG = "keyboard";

    public interface OnKeyboardVisibleChangedListener {
        void onKeyboardVisibleChanged(boolean visible, boolean contentSizeChanged, int keyboardHeight);
    }

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
    public static void assistActivity(Activity activity, OnKeyboardVisibleChangedListener listener) {
        new AndroidBug5497Workaround(activity, listener);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private Activity mActivity;
    private OnKeyboardVisibleChangedListener mListener;
    private boolean mInited = false;
    private int mContentHeight = 0;

    private AndroidBug5497Workaround(Activity activity, OnKeyboardVisibleChangedListener listener) {
        mActivity = activity;
        mListener = listener;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = mChildOfContent.getHeight();
                if (!mInited && height > 0) {
                    mContentHeight = height;
                    mInited = true;
                }
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;

            boolean visible = false;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                visible = true;
                if (mListener != null) {
                    mListener.onKeyboardVisibleChanged(visible, false, heightDifference);
                }
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + StatusBarCompat.getStatusBarHeight(mActivity);
            } else {
                // keyboard probably just became hidden
                if (mListener != null) {
                    mListener.onKeyboardVisibleChanged(visible, false, 0);
                }
                frameLayoutParams.height = mContentHeight;
                //frameLayoutParams.height = getOriginContentHeight(mActivity);
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;

            final boolean show = visible;
            final int keyboardHeight = visible ? heightDifference : 0;
            HandlerUtil.postOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onKeyboardVisibleChanged(show, true, keyboardHeight);
                    }
                }
            });
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    private int getOriginContentHeight(Context context){
        int screenHeight = ScreenUtils.getScreenHeight(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                screenHeight = screenHeight - context.getResources().getDimensionPixelOffset(resId);
            }
        }
        return screenHeight;
    }
}
