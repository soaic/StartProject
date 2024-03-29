package com.soaic.libcommon.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;


public class Utils {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * scale) + 0.5f);
    }

    public static float px2dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return px / scale;
    }

    public static float dp2px(float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                Resources.getSystem().getDisplayMetrics());
    }
}
