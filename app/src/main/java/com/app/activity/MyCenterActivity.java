package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by developserver on 2015/7/29.
 */
public class MyCenterActivity extends Activity {
    private TextView amount;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycenter);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
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
