package com.example.exercise2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mListFragments;

    public SectionsFragmentAdapter(FragmentManager fm) {
        super(fm);
        mListFragments = new ArrayList<Fragment>();
    }

    @Override
    public Fragment getItem(int i) {
        return mListFragments.get(i);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }

    public void addFragment(Fragment fragment){
        mListFragments.add(fragment);
    }

    public List<Fragment> getmListFragments() {
        return mListFragments;
    }

}
