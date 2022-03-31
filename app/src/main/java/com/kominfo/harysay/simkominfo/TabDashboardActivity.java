package com.kominfo.harysay.simkominfo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kominfo.harysay.simkominfo.BuilderManager;
import com.kominfo.harysay.simkominfo.R;
import com.kominfo.harysay.simkominfo.dashboard.ChartsAdapter;
import com.kominfo.harysay.simkominfo.handler.CheckNetwork;
import com.kominfo.harysay.simkominfo.handler.SessionManager;
import com.kominfo.harysay.simkominfo.intro.MyIntro;
import com.kominfo.harysay.simkominfo.login.LoginActivity;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabDashboardActivity extends AppCompatActivity {
    public  static TabLayout tabLayout;
    public  static ViewPager viewPager;
    public  static int int_items = 2;
    private BoomMenuButton bmb2;
    SessionManager session;
    public boolean isFirstStart;

    public TabDashboardActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplication());
        if (CheckNetwork.isInternetAvailable(TabDashboardActivity.this))
        {
            if (!session.isLoggedIn()) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //  Intro App Initialize SharedPreferences
                        SharedPreferences getSharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(getBaseContext());

                        //  Create a new boolean and preference and set it to true
                        isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                        //  Check either activity or app is open very first time or not and do action
                        if (isFirstStart) {

                            //  Launch application introduction screen
                            Intent i = new Intent(TabDashboardActivity.this, MyIntro.class);
                            startActivity(i);
                            SharedPreferences.Editor e = getSharedPreferences.edit();
                            e.putBoolean("firstStart", false);
                            e.apply();
                        }
                    }
                });
                t.start();
                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(TabDashboardActivity.this, LoginActivity.class);

                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                startActivity(i);
                finish();

            } else {
                setContentView(R.layout.tab_dashboard);
                tabLayout=(TabLayout) findViewById(R.id.tabs);
                viewPager=(ViewPager) findViewById(R.id.viewpager);
                viewPager.setOffscreenPageLimit(1);//when you are on the third page, the first one is not destroyed, and vice-versa
                //set an adpater

                viewPager.setAdapter(new ChartsAdapter( getSupportFragmentManager()));

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
                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }else {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(TabDashboardActivity.this).create();

                alertDialog.setTitle("Tidak ada koneksi internet!");
                alertDialog.setMessage("Cek koneksi internet Anda dan ulangi lagi");
                alertDialog.setIcon(android.R.drawable.stat_sys_warning);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            } catch (Exception e) {
                //Log.d(Constants.TAG, "Show Dialog: "+e.getMessage());
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void changeBoomButton(int index) {
        HamButton.Builder builder = (HamButton.Builder) bmb2.getBuilder(index);
        if (index == 0) {
            recreate();
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
            Intent masuk = new Intent(TabDashboardActivity.this, TabMasterDataActivity.class);
            startActivity(masuk);
            finish();
            //builder.unable(true);
        }
    }

}
