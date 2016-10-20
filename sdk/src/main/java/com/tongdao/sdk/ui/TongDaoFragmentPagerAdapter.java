package com.tongdao.sdk.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TongDaoFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> list;

    public TongDaoFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
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
