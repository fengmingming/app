package com.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.R;

/**
 * Created by  on 2015/7/14.
 */
public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.web);
        final WebView wv = (WebView)findViewById(R.id.web);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wv.loadUrl(url);
                return true;
            }
        });
        String url = getIntent().getStringExtra("url");
        if(url != null){
            wv.loadUrl(url);
        }
        Log.i(WebActivity.class.getName(),url);
    }
}
