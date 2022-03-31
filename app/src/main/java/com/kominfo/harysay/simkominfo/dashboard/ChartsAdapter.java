package com.kominfo.harysay.simkominfo.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import static com.kominfo.harysay.simkominfo.TabDashboardActivity.int_items;

/**
 * Created by harysay on 3/4/2017.
 */

public class ChartsAdapter extends FragmentStatePagerAdapter {


 public ChartsAdapter(FragmentManager fm){
     super(fm);
 }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DashboardFragmentAttendChart();
            case 1:
                return  new DaftarKeluhanFragment();
//            case 2:
//                return new LineChartFragment();
//            case 3:
//                return new PiechartFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return int_items;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "DATA PENANGANAN";
            case 1:
                return "Keluhan";
//            case 2:
//                return "Line";
//            case 3:
//                return "Pie";
        }

        return null;
    }
}


