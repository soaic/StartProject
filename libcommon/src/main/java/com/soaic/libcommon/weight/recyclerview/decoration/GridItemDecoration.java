package com.soaic.libcommon.weight.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.soaic.libcommon.AppEnv;
import com.soaic.libcommon.utils.Utils;

/**
 * 绘制 RecycleView Line
 * Simple: GridItemDecoration.newBuilder().build()
 * created by soaic on 2021.3.3
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Builder mBuilder;
    private final Rect mBounds = new Rect();

    private GridItemDecoration(Builder builder) {
        this.mBuilder = builder;
        if (mBuilder == null) {
            mBuilder = new Builder();
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = mBuilder.dividerHeight;
        outRect.bottom = mBuilder.dividerHeight;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        canvas.save();
        drawVertical(canvas, parent);
        drawHorizontal(canvas, parent);
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            mBounds.left = child.getLeft() - params.leftMargin;
            mBounds.right = child.getRight() + params.rightMargin + mBuilder.dividerHeight;
            mBounds.top = child.getBottom() + params.bottomMargin;
            mBounds.bottom = mBounds.top + mBuilder.dividerHeight;
            mBuilder.dividerDrawable.setBounds(mBounds);
            mBuilder.dividerDrawable.draw(canvas);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {

        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            mBounds.left = child.getRight() + params.rightMargin;
            mBounds.right = mBounds.left + mBuilder.dividerHeight ;
            mBounds.top = child.getTop() - params.topMargin;
            mBounds.bottom = child.getBottom() + params.bottomMargin + mBuilder.dividerHeight;
            mBuilder.dividerDrawable.setBounds(mBounds);
            mBuilder.dividerDrawable.draw(canvas);
        }
    }


    public static class Builder {
        /** 分割线Drawable */
        private Drawable dividerDrawable = new ColorDrawable(Color.parseColor("#d8d8d8"));

        /** 分割线高度 */
        private int dividerHeight = dpToPx(10f);

        private int dpToPx(float dp) {
            return Utils.dip2px(AppEnv.getApplicationContext(), dp);
        }

        public Builder setDividerColor(@ColorRes int dividerColor) {
            this.dividerDrawable = new ColorDrawable(ContextCompat.getColor(AppEnv.getApplicationContext(), dividerColor));
            return this;
        }

        public Builder setDividerDrawable(@DrawableRes int dividerDrawable) {
            this.dividerDrawable = ContextCompat.getDrawable(AppEnv.getApplicationContext(), dividerDrawable);
            return this;
        }

        public Builder setDividerHeight(float dividerHeight) {
            this.dividerHeight = dpToPx(dividerHeight);
            return this;
        }

        public GridItemDecoration build() {
            return new GridItemDecoration(this);
        }
    }
}
