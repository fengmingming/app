package com.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
 * Created by on 2015/7/24.
 */
public class CartActivtiy extends Activity {

    private LinearLayout container;
    private Button settle;
    private TextView total;
    private TextView discutPrice;
    private TextView loading;
    private Button goIndex;
    private LinearLayout bottom_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.cart_info));
        container = (LinearLayout) findViewById(R.id.container);
        settle = (Button) findViewById(R.id.settle);
        total = (TextView)findViewById(R.id.total);
        discutPrice = (TextView)findViewById(R.id.discutPrice);
        loading = (TextView)findViewById(R.id.loading);
        goIndex = (Button)findViewById(R.id.goIndex);
        bottom_container = (LinearLayout) findViewById(R.id.bottom_container);
    }

    @Override
    protected void onStart() {
        super.onStart();
        flushCartInfo();
    }

    private void flushCartInfo(){
        container.removeAllViews();
        loading.setVisibility(View.VISIBLE);
        goIndex.setVisibility(View.GONE);
        bottom_container.setVisibility(View.GONE);
        Utils.asyncHttpRequestGet(Constants.URL_CARTINFO,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                com.app.commons.JSONObject jo = new com.app.commons.JSONObject(response);
                if(jo.getBoolean("success")){
                    loading.setVisibility(View.GONE);
                    jo = jo.getJSONObject("result");
                    if(jo != null) jo = jo.getJSONObject("shopCart");
                    if(jo != null){
                        JSONArray ja = jo.getJSONArray("productList");
                        if(ja != null && ja.length() > 0){
                            bottom_container.setVisibility(View.VISIBLE);
                        }
                        total.setText(jo.getString("amount"));
                        discutPrice.setText(jo.getString("discountPrice"));
                        for(int i=0,j=ja.length();i<j;i++){
                            container.addView(createProductInfo(ja.getJSONObject(i)));
                        }
                    }else{
                        goIndex.setVisibility(View.VISIBLE);
                    }
                }else{
                    Toast.makeText(CartActivtiy.this, jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private LinearLayout createProductInfo(com.app.commons.JSONObject jo){
        LinearLayout info = new LinearLayout(CartActivtiy.this);
        info.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        info.setOrientation(LinearLayout.HORIZONTAL);
        info.setGravity(Gravity.CENTER_VERTICAL);
        info.setBackgroundResource(R.drawable.border2);
        RadioButton rb = new RadioButton(CartActivtiy.this);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CartActivtiy.this, getResources().getString(R.string.loading), Toast.LENGTH_SHORT).show();
            }
        });
        info.addView(rb);
        LinearLayout center = new LinearLayout(CartActivtiy.this);
        LinearLayout.LayoutParams centerLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        centerLP.weight = 1;
        center.setOrientation(LinearLayout.VERTICAL);
        center.setLayoutParams(centerLP);
        JSONArray ja = jo.getJSONArray("settleGoods");
        if(ja != null && ja.length() > 0){
            ImageView iv = new ImageView(CartActivtiy.this);
            int wh = Math.round(getResources().getDimension(R.dimen.cart_img_wh));
            iv.setLayoutParams(new ViewGroup.LayoutParams(wh, wh));
            iv.setImageResource(R.drawable.loading);
            Utils.asyncLoadInternetImageView(iv, Constants.URL_IMAGE + "/200X200" + ja.getJSONObject(0).getString("photoUrl"));
            info.addView(iv);
            TextView tv = new TextView(CartActivtiy.this);
            tv.setText(ja.getJSONObject(0).getString("goodsName"));
            center.addView(tv);
            LinearLayout bottom = new LinearLayout(CartActivtiy.this);
            bottom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            bottom.setOrientation(LinearLayout.HORIZONTAL);
            Button sub = new Button(CartActivtiy.this, null, R.style.wrapcontent);
            sub.setText("-");
            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            EditText et = new EditText(CartActivtiy.this);
            et.setText(jo.getString("number"));
            Button add = new Button(CartActivtiy.this, null, R.style.wrapcontent);
            add.setText("+");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            bottom.addView(sub);
            bottom.addView(et);
            bottom.addView(add);
            if(jo.getString("remark") != null){
                TextView remark = new TextView(CartActivtiy.this);
                remark.setText(jo.getString("remark"));
                remark.setTextColor(getResources().getColor(R.color.red));
                bottom.addView(remark);
            }
            center.addView(bottom);
        }
        LinearLayout right = new LinearLayout(CartActivtiy.this);
        right.setOrientation(LinearLayout.VERTICAL);
        TextView tv2 = new TextView(CartActivtiy.this);
        tv2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv2.setGravity(Gravity.CENTER);
        tv2.setText("￥"+jo.getString("amount"));
        tv2.setTextColor(getResources().getColor(R.color.red));
        right.addView(tv2);
        ImageButton ib = new ImageButton(CartActivtiy.this);
        LinearLayout.LayoutParams ibLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ibLP.gravity = Gravity.CENTER;
        ib.setLayoutParams(ibLP);
        ib.setBackgroundColor(getResources().getColor(R.color.white));
        ib.setImageResource(R.drawable.cart_delete);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        right.addView(ib);
        info.addView(center);
        info.addView(right);
        return info;
    }
}
