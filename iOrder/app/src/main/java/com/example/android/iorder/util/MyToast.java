package com.example.android.iorder.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


// Lớp custom các toast
public class MyToast {

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void fastShow(Context context, String message) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }
}
