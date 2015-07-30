package com.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.JSONArray;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by on 2015/7/29.
 */
public class AddressActivity extends Activity {
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
        container = (LinearLayout) findViewById(R.id.container);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.address));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.asyncHttpRequestGet(Constants.URL_ADDRESS, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                if(statusCode == 200){
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        jo = jo.getJSONObject("result");
                        if(jo != null){
                            JSONArray ja = jo.getJSONArray("entry");
                            for(int i=0,j=ja.length();i<j;i++){
                                createContainer(ja.getJSONObject(i));
                            }
                        }
                    }else{
                        Toast.makeText(AddressActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddressActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createContainer(com.app.commons.JSONObject jo){
        LinearLayout.LayoutParams match = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        match.weight = 1;
        LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout h = new LinearLayout(AddressActivity.this);
        h.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        h.setOrientation(LinearLayout.HORIZONTAL);
        h.setBackgroundResource(R.drawable.border2);
        h.setPadding(20,10,10,10);
        h.setGravity(Gravity.CENTER_VERTICAL);
        final CheckBox cb = new CheckBox(AddressActivity.this);
        cb.setButtonDrawable(R.drawable.checkbox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        h.addView(cb);
        LinearLayout center = new LinearLayout(AddressActivity.this);
        center.setLayoutParams(match);
        center.setOrientation(LinearLayout.VERTICAL);
        center.setPadding(10,0,0,0);
        TextView tv = new TextView(AddressActivity.this);
        tv.setText(getResources().getString(R.string.address_detail));
        TextView detail = new TextView(AddressActivity.this);
        detail.setPadding(20,0,0,0);
        TextView receiver = new TextView(AddressActivity.this);
        TextView mobile = new TextView(AddressActivity.this);
        mobile.setPadding(20,0,0,0);
        detail.setText(jo.getString("addressDetail"));
        receiver.setText(jo.getString("receiver"));
        mobile.setText(jo.getString("mobile"));
        LinearLayout center_top = new LinearLayout(AddressActivity.this);
        center_top.setOrientation(LinearLayout.HORIZONTAL);
        center_top.addView(tv);
        center_top.addView(detail);
        LinearLayout center_bottom = new LinearLayout(AddressActivity.this);
        center_bottom.setOrientation(LinearLayout.HORIZONTAL);
        center_bottom.addView(receiver);
        center_bottom.addView(mobile);
        center_bottom.setPadding(0,10,0,0);
        center.addView(center_top);
        center.addView(center_bottom);
        h.addView(center);
        container.addView(h);
    }
}
