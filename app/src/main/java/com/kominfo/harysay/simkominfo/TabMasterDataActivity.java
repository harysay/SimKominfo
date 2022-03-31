package com.kominfo.harysay.simkominfo;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kominfo.harysay.simkominfo.R;
import com.kominfo.harysay.simkominfo.BuilderManager;
import com.kominfo.harysay.simkominfo.dashboard.ChartsAdapter;
import com.kominfo.harysay.simkominfo.masterdata.MasterdataTabAdapter;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabMasterDataActivity extends AppCompatActivity {
    public  static TabLayout tabLayout;
    public  static ViewPager viewPager;
    public  static int int_items = 2;
    private BoomMenuButton bmb2;


    public TabMasterDataActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_masterdata);

        tabLayout=(TabLayout) findViewById(R.id.tabmasterdata);
        viewPager=(ViewPager) findViewById(R.id.viewpager_masterdata);
        viewPager.setOffscreenPageLimit(1);//when you are on the third page, the first one is not destroyed, and vice-versa
        //set an adpater

        viewPager.setAdapter(new MasterdataTabAdapter( getSupportFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        bmb2 = (BoomMenuButton) findViewById(R.id.bmb2);
        assert bmb2 != null;
        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Dashboard", "Halaman utama"));
        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Pengaduan", "Untuk OPD")
                .normalImageRes(R.drawable.elephant));
        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Tindakan", "Untuk Admin dan Pelaksana")
                .normalColorRes(R.color.colorPrimary));
        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Laporan", "Berisi keluhan yang selesai"));
        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Master data", "Untuk menambah user dan opd")
                .unableColor(Color.BLUE)
                .unableImageRes(R.drawable.butterfly)
                .unableText("Unable!"));
        bmb2.setUse3DTransformAnimation(true);
        bmb2.setDuration(122); //menentukan lamanya menu tertampil satuan milisecond
        bmb2.setOnBoomListener(new OnBoomListenerAdapter() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                super.onClicked(index, boomButton);
                changeBoomButton(index);
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik BACK lagi untuk keluar", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);
    }

    private void changeBoomButton(int index) {
        HamButton.Builder builder = (HamButton.Builder) bmb2.getBuilder(index);
        if (index == 0) {
            Intent masuk = new Intent(TabMasterDataActivity.this, TabDashboardActivity.class);
            startActivity(masuk);
            finish();
//            builder.normalText("Changed!");
//            builder.highlightedText("Highlighted, changed!");
//            builder.subNormalText("Sub-text, changed!");
//            builder.normalTextColor(Color.YELLOW);
//            builder.highlightedTextColorRes(R.color.colorPrimary);
//            builder.subNormalTextColor(Color.BLACK);
        } else if (index == 1) {
            builder.normalImageRes(R.drawable.bat);
            builder.highlightedImageRes(R.drawable.bear);
        } else if (index == 2) {
            builder.normalColorRes(R.color.colorAccent);
        } else if (index == 3) {
            builder.pieceColor(Color.WHITE);
        } else if (index == 4) {
            recreate();
//            builder.unable(true);
        }
    }

}
