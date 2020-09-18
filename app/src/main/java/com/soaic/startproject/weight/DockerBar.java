package com.soaic.startproject.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.soaic.startproject.R;
import com.soaic.startproject.base.AppConstants;
import com.soaic.startproject.databinding.DockerBarBinding;
import com.soaic.startproject.databinding.DockerItemBinding;
import com.soaic.libcommon.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class DockerBar extends LinearLayout {
    private static final String TAG = "DockerBar";

    private DockerBarBinding mDockerBarBinding;
    private OnItemClickListener mOnItemClickListener;
    private List<DockerItem> mItems = new ArrayList<>();
    private List<DockerItemBinding> mBindingItemList = new ArrayList<>();


    public interface OnItemClickListener {
        void onItemClick(DockerBar dockerBar, DockerItem item);
    }

    public DockerBar(Context context) {
        this(context, null);
    }

    public DockerBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DockerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    private void init() {
        DockerItem item = new DockerItem(R.string.home, R.drawable.dockbar_home_selector, AppConstants.PageId.INSTANCE.getHome());
        mItems.add(item);
        item = new DockerItem(R.string.classify, R.drawable.dockbar_classify_selector, AppConstants.PageId.INSTANCE.getClassify());
        mItems.add(item);
        item = new DockerItem(R.string.deals, R.drawable.dockbar_deals_selector, AppConstants.PageId.INSTANCE.getDeals());
        mItems.add(item);
        item = new DockerItem(R.string.cart, R.drawable.dockbar_home_cart_selector, AppConstants.PageId.INSTANCE.getCart());
        mItems.add(item);
        item = new DockerItem(R.string.more, R.drawable.dockbar_more_selector, AppConstants.PageId.INSTANCE.getPersonal());
        mItems.add(item);
    }

    private void setupViews() {
        init();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mDockerBarBinding = DataBindingUtil.inflate(inflater, R.layout.docker_bar, this, true);
        mDockerBarBinding.dockerList.removeAllViews();
        mBindingItemList.clear();
        for (int i = 0; i < mItems.size(); i++) {
            final DockerItem item = mItems.get(i);
            DockerItemBinding binding = DockerItemBinding.inflate(inflater, mDockerBarBinding.dockerList, false);
            //binding.icon.setImageResource(item.getIconRes());
            binding.title.setText(item.getTextRes());
            binding.badgeView.setHideOnNull(true);

            LinearLayout.LayoutParams viewParams =
                    new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            viewParams.weight = 1;
            View itemView = binding.getRoot();
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(DockerBar.this, item);
                    }
                }
            });
            if (item.pageId == AppConstants.PageId.INSTANCE.getCart()) {
                binding.icon.setTag(R.id.docker_bar_cart_ico);
            }
            mBindingItemList.add(binding);
            mDockerBarBinding.dockerList.addView(itemView, viewParams);
        }
        setSelection(0);
    }

    public void setSelection(int index) {
        Logger.d(TAG, "setSelection.index = " + index);
        for (int i = 0; i < mBindingItemList.size(); i++) {
            DockerItemBinding binding = mBindingItemList.get(i);
            binding.getRoot().setSelected(i == index);
        }
    }

    public static class DockerItem {

        int pageId;
        int iconRes;
        int textRes;

        public DockerItem(int textRes, int iconRes, int pageId) {
            this.textRes = textRes;
            this.iconRes = iconRes;
            this.pageId = pageId;
        }

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public int getIconRes() {
            return iconRes;
        }

        public void setIconRes(int iconRes) {
            this.iconRes = iconRes;
        }

        public int getTextRes() {
            return textRes;
        }

        public void setTextRes(int textRes) {
            this.textRes = textRes;
        }

    }
}
