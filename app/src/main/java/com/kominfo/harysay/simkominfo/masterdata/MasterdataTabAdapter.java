package com.kominfo.harysay.simkominfo.masterdata;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kominfo.harysay.simkominfo.dashboard.DaftarKeluhanFragment;
import com.kominfo.harysay.simkominfo.dashboard.DashboardFragmentAttendChart;

import static com.kominfo.harysay.simkominfo.TabDashboardActivity.int_items;

/**
 * Created by harysay on 3/4/2017.
 */

public class MasterdataTabAdapter extends FragmentStatePagerAdapter {


 public MasterdataTabAdapter(FragmentManager fm){
     super(fm);
 }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentMasterKendala();
            case 1:
                return  new FragmentMasterOpdRepeater();
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
                return "Master Kendala";
            case 1:
                return "Master OPD/Repeater";
//            case 2:
//                return "Line";
//            case 3:
//                return "Pie";
        }

        return null;
    }
}


