package com.soaic.startproject.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.soaic.startproject.R;

public class SSTImageView extends AppCompatImageView {
    private float mAspectRatio = 0;
    public SSTImageView(Context context) {
        this(context,null);
    }

    public SSTImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SSTImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SSTImageView);
        if(typedArray != null) {
            if (typedArray.hasValue(R.styleable.SSTImageView_viewAspectRatio)) {
                mAspectRatio = typedArray.getFloat(R.styleable.SSTImageView_viewAspectRatio,0);
            }
            typedArray.recycle();
        }
    }

    public void setAspectRatio(float aspectRatio){
        if(mAspectRatio != 0) {
            mAspectRatio = aspectRatio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mAspectRatio != 0) {
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width / mAspectRatio);
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
