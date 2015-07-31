package com.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.MainActivity;
import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.JSONArray;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2015/7/24.
 */
public class CartActivtiy extends Activity {

    private int settleNum = 0;
    private final int def_num = 1;
    private final int def_maxnum = 99;
    private LinearLayout container;
    private Button settle;
    private TextView total;
    private TextView discountPrice;
    private LinearLayout bottom_container;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.cart_info));
        container = (LinearLayout) findViewById(R.id.container);
        settle = (Button) findViewById(R.id.settle);
        settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(settleNum > 0){
                    Utils.asyncHttpRequestGet(Constants.URL_ISNOTLOGIN, null, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                            if(statusCode == 200){
                                com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                                Intent intent = new Intent();
                                if(jo.getBoolean("success")){
                                    intent.setClass(CartActivtiy.this, CommitOrderActivity.class);
                                }else{
                                    intent.setClass(CartActivtiy.this, LoginActivity.class);
                                    intent.putExtra("clazz", CartActivtiy.class.getName());
                                }
                                startActivity(intent);
                            }else{
                                Toast.makeText(CartActivtiy.this, statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(CartActivtiy.this, getResources().getString(R.string.no_commit_cart), Toast.LENGTH_SHORT).show();
                }
            }
        });
        total = (TextView)findViewById(R.id.total);
        discountPrice = (TextView)findViewById(R.id.discountPrice);
        bottom_container = (LinearLayout) findViewById(R.id.bottom_container);
    }

    @Override
    protected void onStart() {
        super.onStart();
        flushCartInfo();
    }

    private void flushCartInfo(){
        settleNum = 0;
        bottom_container.setVisibility(View.GONE);
        Utils.asyncHttpRequestGet(Constants.URL_CARTINFO,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode != 200){
                    Toast.makeText(CartActivtiy.this, statusCode,Toast.LENGTH_SHORT).show();
                    return;
                }
                com.app.commons.JSONObject jo = new com.app.commons.JSONObject(response);
                if(jo.getBoolean("success")){
                    container.removeAllViews();
                    jo = jo.getJSONObject("result");
                    if(jo != null) jo = jo.getJSONObject("shopCart");
                    if(jo != null){
                        flag = jo.getBoolean("flag");
                        JSONArray ja = jo.getJSONArray("productList");
                        if(ja != null && ja.length() > 0){
                            bottom_container.setVisibility(View.VISIBLE);
                        }
                        total.setText(jo.getString("amount"));
                        discountPrice.setText(jo.getString("discountPrice"));
                        LinearLayout cart;
                        for(int i=0,j=ja.length();i<j;i++){
                            cart = createProductInfo(ja.getJSONObject(i));
                            if(cart != null){
                                container.addView(cart);
                            }
                        }
                    }else{
                            Button btn = new Button(CartActivtiy.this);
                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            llp.gravity = Gravity.CENTER;
                            llp.setMargins(0,100,0,0);
                            btn.setLayoutParams(llp);
                            btn.setText(getResources().getString(R.string.no_cart_info));
                            btn.setBackgroundResource(R.drawable.btn_primary);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setClass(CartActivtiy.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            container.addView(btn);
                    }
                }else{
                    Toast.makeText(CartActivtiy.this, jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private LinearLayout createProductInfo(com.app.commons.JSONObject jo){
        JSONArray ja = jo.getJSONArray("settleGoods");
        if(ja == null || ja.length()==0){
            return null;
        }
        final String productSku = jo.getString("productSku");
        LinearLayout info = new LinearLayout(CartActivtiy.this);
        info.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        info.setOrientation(LinearLayout.HORIZONTAL);
        info.setGravity(Gravity.CENTER_VERTICAL);
        info.setBackgroundResource(R.drawable.border2);
        final CheckBox rb = new CheckBox(CartActivtiy.this);
        rb.setButtonDrawable(R.drawable.checkbox);
        if(jo.getBoolean("checked")){
            rb.setChecked(true);
            settleNum = settleNum + 1;
        }else{
            rb.setChecked(false);
        }
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Map<String,Object> param = new HashMap<>();
                param.put("ids", productSku);
                param.put("checkeds", rb.isChecked()?"Y":"N");
                Utils.asyncHttpRequestPost(Constants.URL_CART_CHANGESTATUS, param, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                        if(statusCode == 200){
                            if(jo.getBoolean("success")){
                                flushCartInfo();
                            }else{
                                Toast.makeText(CartActivtiy.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                                flushCartInfo();
                            }
                        }else {
                            Toast.makeText(CartActivtiy.this, statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        info.addView(rb);
        LinearLayout center = new LinearLayout(CartActivtiy.this);
        LinearLayout.LayoutParams centerLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        centerLP.weight = 1;
        center.setOrientation(LinearLayout.VERTICAL);
        center.setLayoutParams(centerLP);
        center.setPadding(10,0,0,0);
        if(ja != null && ja.length() > 0){
            ImageView iv = new ImageView(CartActivtiy.this);
            int wh = Math.round(getResources().getDimension(R.dimen.cart_img_wh));
            iv.setLayoutParams(new ViewGroup.LayoutParams(wh, wh));
            iv.setImageResource(R.drawable.loading);
            Utils.asyncLoadInternetImageView(iv, Constants.URL_IMAGE + "/200X200" + ja.getJSONObject(0).getString("photoUrl"));
            info.addView(iv);
            TextView tv = new TextView(CartActivtiy.this);
            if(ja.length() == 1){
                tv.setText(ja.getJSONObject(0).getString("goodsName"));
            }else{
                tv.setText(getResources().getString(R.string.goods_set));
            }
            center.addView(tv);
            LinearLayout bottom = new LinearLayout(CartActivtiy.this);
            bottom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            bottom.setOrientation(LinearLayout.HORIZONTAL);
            bottom.setPadding(0,15,0,0);
            LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            wrap.setMargins(0,0,0,0);
            final EditText et = new EditText(CartActivtiy.this);
            et.setBackgroundResource(R.drawable.wrapcontent);
            et.setText(jo.getString("number"));
            et.setSingleLine(true);
            et.setKeyListener(new DigitsKeyListener(false,false));
            et.setMaxWidth(50);
            et.setMinWidth(50);
            et.setGravity(Gravity.RIGHT);
            et.setPadding(5,2,5,2);
            et.setFocusable(true);
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.length() == 0){
                        editable.append(String.valueOf(def_num));
                    }else if(Integer.parseInt(editable.toString()) <= 0){
                        editable.replace(0, 1, String.valueOf(def_num));
                    }else if(editable.length() > 2){
                        editable.replace(0, editable.length(),String.valueOf(def_maxnum));
                    }
                    Map<String,Object> param = new HashMap<>();
                    param.put("id",productSku);
                    param.put("count",editable.toString());
                    Utils.asyncHttpRequestPost(Constants.URL_CART_CHANGENUMBER, param, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if(statusCode == 200){
                                if(jo.getBoolean("success")){
                                    flushCartInfo();
                                }else{
                                    Toast.makeText(CartActivtiy.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                                    flushCartInfo();
                                }
                            }else {
                                Toast.makeText(CartActivtiy.this, statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            Button sub = new Button(CartActivtiy.this, null, R.style.wrapcontent);
            sub.setBackgroundResource(R.drawable.wrapcontent);
            sub.setText("-");
            sub.setPadding(30,2,30,2);
            sub.setLayoutParams(wrap);
            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int num = Integer.parseInt(et.getText().toString());
                    if(num > 1){
                        et.setText(String.valueOf((num - 1)));
                    }
                }
            });
            Button add = new Button(CartActivtiy.this, null, R.style.wrapcontent);
            add.setBackgroundResource(R.drawable.wrapcontent);
            add.setText("+");
            add.setPadding(30,2,30,2);
            add.setLayoutParams(wrap);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer num = Integer.parseInt(et.getText().toString());
                    et.setText(String.valueOf(num + 1));
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
        right.setGravity(Gravity.CENTER_VERTICAL);
        right.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv2 = new TextView(CartActivtiy.this);
        LinearLayout.LayoutParams bottom = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottom.gravity = Gravity.BOTTOM;
        tv2.setLayoutParams(bottom);
        tv2.setText("￥"+jo.getString("amount"));
        tv2.setTextColor(getResources().getColor(R.color.red));
        right.addView(tv2);
        ImageButton ib = new ImageButton(CartActivtiy.this);
        LinearLayout.LayoutParams ibLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ibLP.gravity = Gravity.RIGHT;
        ibLP.setMargins(0,0,0,0);
        ib.setLayoutParams(ibLP);
        ib.setScaleType(ImageView.ScaleType.FIT_XY);
        ib.setBackgroundResource(R.drawable.border3);
        ib.setPadding(0,0,0,0);
        ib.setImageResource(R.drawable.cart_delete);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("ids",productSku);
                Utils.asyncHttpRequestPost(Constants.URL_CART_DELETEPRODUCTS, param, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                        if(statusCode == 200){
                            if(jo.getBoolean("success")){
                                flushCartInfo();
                            }else{
                                Toast.makeText(CartActivtiy.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                                flushCartInfo();
                            }
                        }else {
                            Toast.makeText(CartActivtiy.this, statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        right.addView(ib);
        info.addView(center);
        info.addView(right);
        return info;
    }
}
