package com.soaic.libcommon.weight.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ComplexColorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.soaic.libcommon.AppEnv;
import com.soaic.libcommon.utils.Utils;

/**
 * 绘制 RecycleView Line
 * Simple: DividerItemDecoration.newBuilder().build()
 * created by soaic on 2019.11.29
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Builder mBuilder;
    private final Paint mPaint;
    private final Rect mBounds = new Rect();

    private DividerItemDecoration(Builder builder) {
        this.mBuilder = builder;
        if (mBuilder == null) {
            mBuilder = new Builder();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBuilder.dividerColor);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + mBuilder.marginLeft;
            right = parent.getWidth() - parent.getPaddingRight() - mBuilder.marginRight;
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = mBuilder.marginLeft;
            right = parent.getWidth() - mBuilder.marginRight;
        }

        final int childCount = parent.getChildCount() - (mBuilder.isDrawLastLine ? 0 : 1);
        for (int i = mBuilder.startPosition; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top;
            if (!mBuilder.isDrawFirstLine && i == 0) {
                top = bottom;
            } else {
                top = bottom - mBuilder.dividerHeight;
            }

            canvas.drawRect(left, top, right, bottom, mPaint);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, 0, mBuilder.dividerHeight);
    }

    public static class Builder {
        /** 分割线颜色 */
        private int dividerColor = Color.parseColor("#d8d8d8");

        /** 分割线高度 */
        private int dividerHeight = dpToPx(0.5f);

        /** 距离左边 */
        private int marginLeft = 0;

        /** 距离右边 */
        private int marginRight = 0;

        /** 是否绘制最后一行 */
        private boolean isDrawLastLine = true;

        /** 是否绘制第一行 */
        private boolean isDrawFirstLine = true;

        /** 开始绘制行 */
        private int startPosition = 0;

        private int dpToPx(float dp) {
            return Utils.dip2px(AppEnv.getApplicationContext(), dp);
        }

        public Builder setDividerColor(@ColorRes int dividerColor) {
            this.dividerColor = ContextCompat.getColor(AppEnv.getApplicationContext(), dividerColor);
            return this;
        }

        public Builder setDividerHeight(float dividerHeight) {
            this.dividerHeight = dpToPx(dividerHeight);
            return this;
        }

        public Builder setMarginLeft(int marginLeft) {
            this.marginLeft = dpToPx(marginLeft);
            return this;
        }

        public Builder setMarginRight(int marginRight) {
            this.marginRight = dpToPx(marginRight);
            return this;
        }

        public Builder setDrawLastLine(boolean drawLastLine) {
            isDrawLastLine = drawLastLine;
            return this;
        }

        public Builder setDrawFirstLine(boolean drawFirstLine) {
            isDrawFirstLine = drawFirstLine;
            return this;
        }

        public Builder setStartPosition(int startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public DividerItemDecoration build() {
            return new DividerItemDecoration(this);
        }
    }
}
