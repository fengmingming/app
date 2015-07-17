package com.app.activity;

import android.app.Activity;
import android.os.Bundle;

import com.app.R;
import com.app.Title;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by developserver on 2015/7/17.
 */
public class GoodsDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gdetail);
        String url = getIntent().getStringExtra("url");
        Utils.asyncHttpRequestGet(url, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Title title = (Title) getFragmentManager().findFragmentById(R.id.gdetail_title);
        title.setTitle(getResources().getString(R.string.goodsdetail));
    }
}
