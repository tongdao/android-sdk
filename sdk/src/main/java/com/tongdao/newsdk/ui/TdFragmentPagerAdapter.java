package com.tongdao.newsdk.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TdFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> list;

    public TdFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return this.list.get(arg0);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

}
