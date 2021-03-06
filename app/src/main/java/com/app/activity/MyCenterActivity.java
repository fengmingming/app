package com.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.MainActivity;
import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by developserver on 2015/7/29.
 */
public class MyCenterActivity extends FragmentActivity {
    private TextView amount;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycenter);
        Title title = (Title) getSupportFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.mysls));
        amount = (TextView) findViewById(R.id.my_amount);
        username = (TextView) findViewById(R.id.my_username);
        TextView address = (TextView) findViewById(R.id.my_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MyCenterActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });
        TextView myorders = (TextView)findViewById(R.id.my_orders);
        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MyCenterActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
        TextView couponBtn = (TextView)findViewById(R.id.couponBtn);
        couponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MyCenterActivity.this, CouponActivity.class);
                startActivity(intent);
            }
        });
        TextView security = (TextView)findViewById(R.id.security);
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MyCenterActivity.this, SecurityActivity.class);
                startActivity(intent);
            }
        });
        Button unlogin = (Button)findViewById(R.id.unlogin);
        unlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.asyncHttpRequestGet(Constants.URL_LOGOUT,null,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if(statusCode == 200){
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if(jo.getBoolean("success")){
                                Intent intent = new Intent();
                                intent.setClass(MyCenterActivity.this, MainActivity.class );
                                startActivity(intent);
                            }else{
                                Toast.makeText(MyCenterActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MyCenterActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.asyncHttpRequestGet(Constants.URL_PCENTER, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                if(statusCode == 200){
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        jo = jo.getJSONObject("result");
                        if(jo != null){
                            username.setText(jo.getString("username"));
                            amount.setText(jo.getString("amount"));
                        }
                    }else{
                        Toast.makeText(MyCenterActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MyCenterActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
