package com.soaic.libcommon.base;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager通用Adapter
 * Created by XiaoSai on 2016/7/4.
 */
public class BasicPagerAdapter extends PagerAdapter {
    
    protected ArrayList<View> mData = new ArrayList<>();

    public BasicPagerAdapter(){ }

    public BasicPagerAdapter(List<View> arrays){
        this.mData.clear();
        this.mData.addAll(arrays);
    }

    public void setData(List<View> arrays){
        this.mData.clear();
        this.mData.addAll(arrays);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mData.get(getRealPosition(position)));
        return mData.get(getRealPosition(position));
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public int getRealPosition(int position){
        return position > 0 ? position % getCount() : position;
    }
}
