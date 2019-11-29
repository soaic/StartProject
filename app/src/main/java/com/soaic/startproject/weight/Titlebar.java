package com.soaic.startproject.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.soaic.libcommon.utils.KeyboardUtils;
import com.soaic.libcommon.utils.StatusBarCompat;
import com.soaic.libcommon.utils.Utils;
import com.soaic.startproject.R;
import com.soaic.startproject.databinding.TitlebarViewBinding;

/**
 * Created by nix on 16/1/2.
 */
public class Titlebar extends LinearLayout implements View.OnClickListener {

    private TitlebarViewBinding mBinding;

    private String mLeftActionText;
    private int mLeftActionImgRes;
    private String mRightActionText;
    private int mRightActionImgRes;

    private OnItemClickListener mListener;
    private Object mContentTag = "content";
    private int mRightActionSecondImgRes;
    private String mRightActionSecondText;

    public interface OnItemClickListener {
        void onTitlebarItemClick(Titlebar titlebar, View itemView);
    }

    public Titlebar(Context context) {
        this(context, null);
    }

    public Titlebar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(attrs);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }

    private void setupViews(AttributeSet attrs) {
        mBinding = TitlebarViewBinding.inflate(LayoutInflater.from(getContext()), this, true);
        int statusBarHeight = StatusBarCompat.getStatusBarHeight(getContext());
        LinearLayout.LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.titlebar_height) + statusBarHeight);
        mBinding.rootView.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, statusBarHeight);
        mBinding.statusbarPadding.setLayoutParams(params2);

        mBinding.rootView.setBackgroundResource(R.drawable.titlebar_bg);

        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Titlebar, 0, 0);
            if (a.hasValue(R.styleable.Titlebar_titleName)) {
                String text = a.getString(R.styleable.Titlebar_titleName);
                setTitle(text);
            }

            if (a.hasValue(R.styleable.Titlebar_titleImage)) {
                mBinding.titleImage.setImageDrawable(a.getDrawable(R.styleable.Titlebar_titleImage));
            }

            if (a.hasValue(R.styleable.Titlebar_leftImage)) {
                setLeftActionImage(a.getResourceId(R.styleable.Titlebar_leftImage, 0));
            }
            if (a.hasValue(R.styleable.Titlebar_leftText)) {
                setLeftActionText(a.getString(R.styleable.Titlebar_leftText));
            }

            if (a.hasValue(R.styleable.Titlebar_rightImage)) {
                setRightActionImage(a.getResourceId(R.styleable.Titlebar_rightImage, 0));
            }
            if (a.hasValue(R.styleable.Titlebar_rightText)) {
                setRightActionText(a.getString(R.styleable.Titlebar_rightText));
            }

            if (a.hasValue(R.styleable.Titlebar_rightSecondImage)) {
                setRightActionSecondImage(a.getResourceId(R.styleable.Titlebar_rightSecondImage, 0));
            }
            if (a.hasValue(R.styleable.Titlebar_rightSecondText)) {
                setRightActionSecondText(a.getString(R.styleable.Titlebar_rightSecondText));
            }

            a.recycle();
        }

        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    KeyboardUtils.hideSoftInput(getContext(),mBinding.getRoot());
                }
            }
        });
    }

    public void setBackground(@ColorInt int color) {
        mBinding.rootView.setBackgroundColor(color);
    }

    public void setBackgourd(@DrawableRes int resId) {
        ViewGroup.LayoutParams params = mBinding.rootView.getLayoutParams();
        if (params != null) {
            params.height = getResources().getDimensionPixelOffset(R.dimen.titlebar_height) + StatusBarCompat.getStatusBarHeight(getContext());
        }
        mBinding.rootView.setBackgroundResource(resId);
    }

    public void setTitle(int textRes) {
        setTitle(getContext().getResources().getString(textRes));
    }

    public void setTitle(String title) {
        if (title == null) {
            mBinding.title.setText("");
        } else {
            mBinding.title.setText(title);
        }
    }

    public void setTitleImage(@DrawableRes int resId) {
        mBinding.titleImage.setImageResource(resId);
    }

    public void setRightActionImage(int imgRes) {
        mRightActionImgRes = imgRes;
        updateRightAction();
    }


    public void setRightActionText(int textResId) {
        mRightActionText = getContext().getString(textResId);
        updateRightAction();
    }

    public void setRightActionText(String text) {
        mRightActionText = text;
        updateRightAction();
    }

    public void setRightActionSecondText(String text) {
        mRightActionSecondText = text;
        updateRightSecondAction();
    }
    public void setRightActionSecondImage(int imgRes) {
        mRightActionSecondImgRes = imgRes;
        updateRightSecondAction();
    }


    // public void setRightActionTextColor(int color) {
    // mBinding.rightActionTextView.setTextColor(color);
    // }

    private void updateRightAction() {
        boolean validText = !TextUtils.isEmpty(mRightActionText);
        mBinding.rightActionTextView.setText(validText ? mRightActionText : "");
        mBinding.rightActionTextView.setVisibility(validText ? View.VISIBLE : View.GONE);

        boolean validImag = (mRightActionImgRes != 0);
        if (validImag) {
            mBinding.rightActionImageView.setVisibility(View.VISIBLE);
            mBinding.rightActionImageView.setImageResource(mRightActionImgRes);
        } else {
            mBinding.rightActionImageView.setVisibility(View.GONE);
        }

        if (validText || validImag) {
            mBinding.rightAction.setOnClickListener(this);
        } else {
            mBinding.rightAction.setOnClickListener(null);
        }
    }

    private void updateRightSecondAction() {
        boolean validText = !TextUtils.isEmpty(mRightActionSecondText);
        mBinding.rightActionSecondTextView.setText(validText ? mRightActionSecondText : "");
        mBinding.rightActionSecondTextView.setVisibility(validText ? View.VISIBLE : View.GONE);

        boolean validImag = (mRightActionSecondImgRes != 0);
        if (validImag) {
            mBinding.rightActionSecondImageView.setVisibility(View.VISIBLE);
            mBinding.rightActionSecondImageView.setImageResource(mRightActionSecondImgRes);
        } else {
            mBinding.rightActionSecondImageView.setVisibility(View.GONE);
        }

        if (validText || validImag) {
            mBinding.rightSecondAction.setOnClickListener(this);
        } else {
            mBinding.rightSecondAction.setOnClickListener(null);
        }
    }

    public void setLeftActionImage(int imgRes) {
        mLeftActionImgRes = imgRes;
        updateLeftAction();
    }

    public void setLeftActionText(int textResId) {
        mLeftActionText = getContext().getString(textResId);
        updateLeftAction();
    }

    public void setLeftActionText(String text) {
        mLeftActionText = text;
        updateLeftAction();
    }

    public void setLeftCustomActionView(View rightView) {
        mBinding.rightAction.setPadding(0, 0, Utils.dip2px(getContext(),15), 0);
        mBinding.leftAction.removeAllViews();
        mBinding.leftAction.addView(rightView);
        mBinding.leftAction.setOnClickListener(this);
    }

    public void setRightCustomActionView(View rightView) {
        mBinding.rightAction.setPadding(Utils.dip2px(getContext(),15), 0, 0, 0);
        mBinding.rightAction.removeAllViews();
        mBinding.rightAction.addView(rightView);
        mBinding.rightAction.setOnClickListener(this);
    }

    public void setLeftActionEnable(boolean enable) {
        mBinding.leftAction.setEnabled(enable);
    }

    public void setRightActionEnable(boolean enable) {
        mBinding.rightAction.setEnabled(enable);
    }

    private void updateLeftAction() {
        boolean validText = !TextUtils.isEmpty(mLeftActionText);
        mBinding.leftActionTextView.setText(validText ? mLeftActionText : "");
        mBinding.leftActionTextView.setVisibility(validText ? View.VISIBLE : View.GONE);

        boolean validImag = (mLeftActionImgRes != 0);
        if (validImag) {
            mBinding.leftActionImageView.setVisibility(View.VISIBLE);
            mBinding.leftActionImageView.setImageResource(mLeftActionImgRes);
        } else {
            mBinding.leftActionImageView.setVisibility(View.GONE);
        }

        if (validText || validImag) {
            mBinding.leftAction.setOnClickListener(this);
        } else {
            mBinding.leftAction.setOnClickListener(null);
        }
    }

    public void startMarquee(){

        mBinding.title.setSingleLine(true);
        mBinding.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mBinding.title.setMarqueeRepeatLimit(-1);
        //mBinding.title.setTextIsSelectable(true);
        mBinding.title.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onTitlebarItemClick(this, v);
        }
    }
}
