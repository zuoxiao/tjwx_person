package com.example.tjwx_person.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cxcl.property.customer.BaseActivity;
import com.cxcl.property.customer.R;

/**
 * Created by zuo on 2016/6/26.
 */
public class PricelistActivity extends BaseActivity {


    WebView priceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_price_list, 1, "价目表");

        priceList =
                (WebView) findViewById(R.id.priceList);


        priceList.loadUrl("file:///android_asset/jiamu01.html");
        priceList.setWebViewClient(new WebViewClient());
        WebSettings webSettings = priceList.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        priceList.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        priceList.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        priceList.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        priceList.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        priceList.getSettings().setLoadWithOverviewMode(true);

    }

    @Override
    public void backButton() {
        super.backButton();
        if (priceList.canGoBack()){
            priceList.goBack();
        }else {
            this.finish();
        }


    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK &&priceList.canGoBack()){
//            priceList.goBack();
//            return true;
//        }else {
//            this.finish();
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}
