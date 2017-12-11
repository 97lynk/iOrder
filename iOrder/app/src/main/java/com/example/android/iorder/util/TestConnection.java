package com.example.android.iorder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// Lớp kiểm tra liệu có kết nối internet không
public class TestConnection {
    public static boolean haveNetWorkConnection(Context context) {
        boolean haveConnWifi = false;
        boolean haveConnMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
        for (NetworkInfo info : networkInfo) {
            if (info.getTypeName().equalsIgnoreCase("WIFI") &&
                    info.isConnected()) {
                haveConnWifi = true;
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE") &&
                    info.isConnected()) {
                haveConnWifi = true;
            }
        }
        return haveConnMobile || haveConnWifi;
    }
}
