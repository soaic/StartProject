package com.soaic.libcommon.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * FragmentPager通用Adapter
 * Created by Soaic on 2019/9/3.
 */
public class BasicFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<T> fragments;
    
    public BasicFragmentPagerAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public T getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
