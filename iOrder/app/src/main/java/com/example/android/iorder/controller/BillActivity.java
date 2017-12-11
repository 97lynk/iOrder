package com.example.android.iorder.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.iorder.R;
import com.example.android.iorder.util.MyDomain;


public class BillActivity extends AppCompatActivity {

    Toolbar toolBarBill;
    WebView wvBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        initializeControls();
        addEvents();
    }

    private void initializeControls() {
        wvBill = (WebView) findViewById(R.id.wvBill);
        toolBarBill = (Toolbar) findViewById(R.id.toolBarBill);

        wvBill.setWebViewClient(new MyBrowser());
        wvBill.getSettings().setLoadsImagesAutomatically(true);
        wvBill.getSettings().setJavaScriptEnabled(true);
        wvBill.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvBill.loadUrl(MyDomain.URL_SERVICE);
    }

    private void addEvents() {
        actionBar();
    }


    private void actionBar() {
        setSupportActionBar(toolBarBill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarBill.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
