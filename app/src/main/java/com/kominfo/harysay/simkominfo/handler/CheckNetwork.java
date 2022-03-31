package com.kominfo.harysay.simkominfo.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Ron on 15-08-2017.
 */

public class CheckNetwork {
    private static final String TAG = "TabDashboardActivity";

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            Log.d(TAG, "Tidak ada koneksi internet!");
            return false;
        } else {
            if (info.isConnected()) {
                Log.d(TAG, " Koneksi internet tersedia...");
                return true;
            } else {
                Log.d(TAG, " Koneksi internet");
                return true;
            }
        }
    }
}
