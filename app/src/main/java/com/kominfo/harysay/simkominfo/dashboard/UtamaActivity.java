package com.kominfo.harysay.simkominfo.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kominfo.harysay.simkominfo.R;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;

public class UtamaActivity extends AppCompatActivity {

    public TextView durationTextView;
    public BoomMenuButton bmb2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);

//        bmb2 = (BoomMenuButton) findViewById(R.id.bmb2);
//        assert bmb2 != null;
//        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Dashboard", "Halaman utama"));
//        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Pengaduan", "Untuk OPD")
//                .normalImageRes(R.drawable.elephant));
//        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Tindakan", "Untuk Admin dan Pelaksana")
//                .normalColorRes(R.color.colorPrimary));
//        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Laporan", "Berisi keluhan yang selesai"));
//        bmb2.addBuilder(BuilderManager.getHamButtonBuilder("Master data", "Untuk menambah user dan opd")
//                .unableColor(Color.BLUE)
//                .unableImageRes(R.drawable.butterfly)
//                .unableText("Unable!"));
//        bmb2.setUse3DTransformAnimation(true);
//        bmb2.setDuration(122); //menentukan lamanya menu tertampil satuan milisecond
//        bmb2.setOnBoomListener(new OnBoomListenerAdapter() {
//            @Override
//            public void onClicked(int index, BoomButton boomButton) {
//                super.onClicked(index, boomButton);
//                changeBoomButton(index);
//            }
//        });

    }

    public class deklarasiBoomMenu{
            public void menuBoomPakai(){

            }
    }

//    private void changeBoomButton(int index) {
//        HamButton.Builder builder = (HamButton.Builder) bmb2.getBuilder(index);
//        if (index == 0) {
//            builder.normalText("Changed!");
//            builder.highlightedText("Highlighted, changed!");
//            builder.subNormalText("Sub-text, changed!");
//            builder.normalTextColor(Color.YELLOW);
//            builder.highlightedTextColorRes(R.color.colorPrimary);
//            builder.subNormalTextColor(Color.BLACK);
//        } else if (index == 1) {
//            builder.normalImageRes(R.drawable.bat);
//            builder.highlightedImageRes(R.drawable.bear);
//        } else if (index == 2) {
//            builder.normalColorRes(R.color.colorAccent);
//        } else if (index == 3) {
//            builder.pieceColor(Color.WHITE);
//        } else if (index == 4) {
//            builder.unable(true);
//        }
//    }
}