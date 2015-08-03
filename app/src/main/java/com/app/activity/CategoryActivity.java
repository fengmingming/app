package com.app.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.JSONArray;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by developserver on 2015/7/22.
 */
public class CategoryActivity extends Activity {
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        container = (LinearLayout) findViewById(R.id.container);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.category_title));
        Utils.asyncHttpRequestGet(Constants.URL_CATEGORY_JSON, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                JSONArray ja = jo.getJSONArray("category");
                createCategory(ja);
            }
        });
    }

    private void createCategory(JSONArray ja){
        LinearLayout.LayoutParams left = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams right = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        right.weight = 1;
        for(int i=0,j=ja.length();i<j;i++){
            final com.app.commons.JSONObject jo = ja.getJSONObject(i);
            LinearLayout ll = new LinearLayout(CategoryActivity.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setBackgroundResource(R.drawable.border2);
            ll.setPadding(5,10,5,10);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv = new TextView(CategoryActivity.this);
            tv.setTextColor(getResources().getColor(R.color.def_fontcolor));
            tv.setText(jo.getString("fne"));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initListActivity(jo.getString("fid"),null,jo.getString("fne"));
                }
            });
            tv.setLayoutParams(left);
            ll.addView(tv);
            container.addView(ll);
            JSONArray ja2 = jo.getJSONArray("saray");
            if(ja2 != null && ja2.length() > 0){
                final LinearLayout second = createCategory2(ja2);
                final AtomicBoolean isShow = new AtomicBoolean(false);
                TextView tv2 = new TextView(CategoryActivity.this);
                tv.setTextColor(getResources().getColor(R.color.def_fontcolor));
                tv2.setText("∨");
                tv2.setTextColor(getResources().getColor(R.color.def_fontcolor));
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isShow.get()){
                            second.setVisibility(View.GONE);
                            isShow.set(false);
                        }else{
                            second.setVisibility(View.VISIBLE);
                            isShow.set(true);
                        }
                    }
                });
                tv2.setGravity(Gravity.RIGHT);
                tv2.setLayoutParams(right);
                ll.addView(tv2);
                container.addView(second);
            }
        }
    }

    private LinearLayout createCategory2(JSONArray ja){
        TableLayout v = new TableLayout(CategoryActivity.this);
        v.setVisibility(View.GONE);
        v.setStretchAllColumns(true);
        v.setBackgroundColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vlp.setMargins(2, 1, 2, 1);
        v.setLayoutParams(vlp);
        TableRow h = null;
        for(int i=0,j=ja.length();i<j;i++){
            if(i%3 == 0){
                h = new TableRow(CategoryActivity.this);
                h.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                v.addView(h);
            }
            final com.app.commons.JSONObject jo = ja.getJSONObject(i);
            TextView tv = new TextView(CategoryActivity.this);
            tv.setTextColor(getResources().getColor(R.color.def_fontcolor));
            tv.setGravity(Gravity.CENTER);
            tv.setText(jo.getString("sne"));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initListActivity(jo.getString("sid"),null,jo.getString("sne"));
                }
            });
            h.addView(tv);
        }
        return v;
    }

    private void initListActivity(String fid,String sid,String name){
        String url = null;
        if(fid != null){
            url = Constants.URL_CATEGORY_GOODS_LIST + "?first="+fid;
        }else if(sid != null){
            url = Constants.URL_CATEGORY_GOODS_LIST + "?second="+sid;
        }
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("title", name);
        intent.setClass(CategoryActivity.this, ListActivity.class);
        startActivity(intent);
    }


}
