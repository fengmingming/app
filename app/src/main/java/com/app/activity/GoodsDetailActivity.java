package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.Title;
import com.app.adapter.LoopImgsAdapter;
import com.app.commons.Constants;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.commons.JSONObject;
import com.app.commons.JSONArray;

/**
 * Created by developserver on 2015/7/17.
 */
public class GoodsDetailActivity extends Activity {
    private Long goodsId;
    private volatile int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gdetail);
        ImageButton skipCart = (ImageButton)findViewById(R.id.cartBtn);
        skipCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GoodsDetailActivity.this, CartActivtiy.class);
                startActivity(intent);
            }
        });
        number = Integer.parseInt(getResources().getString(R.string.default_buy_number));
        final String url = getIntent().getStringExtra("url");
        loadData(url);
        getCartNumber();
        final EditText et = (EditText) findViewById(R.id.gd_number);
        final Button addBtn = (Button) findViewById(R.id.gd_add);
        final Button subBtn = (Button) findViewById(R.id.gd_sub);
        final Button addCartBtn = (Button) findViewById(R.id.addCartBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number < 99){
                    et.setText(String.valueOf(number + 1));
                }
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number > 1){
                    et.setText(String.valueOf(number - 1));
                }
            }
        });
        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String numStr = et.getText().toString();
                if("".equals(numStr)){
                    et.setText(getResources().getString(R.string.default_buy_number));
                    return;
                }
                int num = Integer.parseInt(numStr);
                if (num > 0 && num < 100) {
                    number = num;
                } else {
                    Toast.makeText(GoodsDetailActivity.this, "每次商品购买数量1-100", Toast.LENGTH_SHORT).show();
                    et.setText(getResources().getString(R.string.default_buy_number));
                }
            }
        });
        //下拉刷新
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) findViewById(R.id.gd_flush);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                loadData(url);
            }
        });
    }

    private void loadData(String url){
        final TextView gname = (TextView) findViewById(R.id.gd_gname);
        final TextView price = (TextView) findViewById(R.id.gd_price);
        final TextView marketPrice = (TextView) findViewById(R.id.gd_marketPrice);
        final ViewPager vp = (ViewPager) findViewById(R.id.gd_photo_vp);
        final WebView wv = (WebView) findViewById(R.id.gdetail_info);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setSupportZoom(true);
        Utils.asyncHttpRequestGet(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject resObject) {
                if(statusCode == 200){
                    try{
                        com.app.commons.JSONObject res = new com.app.commons.JSONObject(resObject);
                        if(res.getBoolean("success")){
                            JSONObject jo = res.getJSONObject("result");
                            if(jo != null && ! "".equals(jo)){
                                JSONArray urls = jo.getJSONArray("urls");
                                jo = jo.getJSONObject("goods");
                                if(jo != null){
                                    GoodsDetailActivity.this.goodsId = jo.getLong("id");
                                    gname.setText(jo.getString("goodsName"));
                                    price.setText(jo.getString("price"));
                                    marketPrice.setText(jo.getString("marketPrice"));
                                    marketPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                    List<ImageView> imgs = new ArrayList<ImageView>();
                                    String url;
                                    for(int i=0,j = urls.length();i<j;i++){
                                        url = urls.getString(i);
                                        if(url != null && !"".equals(url)){
                                            ImageView iv = new ImageView(GoodsDetailActivity.this);
                                            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                            Utils.asyncLoadInternetImageView(iv, Constants.URL_IMAGE + url);
                                            imgs.add(iv);
                                        }
                                    }
                                    vp.setAdapter(new LoopImgsAdapter(imgs));
                                    String description = jo.getString("description");
                                    if(description != null && !"".equals(description)){
                                        wv.loadData(MessageFormat.format(Constants.DETAIL_CONTANT_WRAP, jo.getString("description")), "text/html", "utf-8");
                                    }
                                }
                            }
                        }else{
                            Toast.makeText(GoodsDetailActivity.this, res.getString("errMsg"), Toast.LENGTH_SHORT);
                        }
                    }catch(Exception e){
                        Log.e(GoodsDetailActivity.class.getName(), e.getMessage(), e);
                        Toast.makeText(GoodsDetailActivity.this, "系统异常请重试", Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, org.json.JSONObject errorResponse) {
                Toast.makeText(GoodsDetailActivity.this, "系统异常请重试", Toast.LENGTH_SHORT);
            }
        });
    }

    private void getCartNumber(){
        final TextView tv = (TextView) findViewById(R.id.gd_cart_number);
        Utils.asyncHttpRequestGet(Constants.URL_CART_NUMBER, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject res) {
                JSONObject jo = new JSONObject(res);
                if(statusCode == 200&&jo.getBoolean("success")){
                    tv.setText(jo.getString("result"));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Title title = (Title) getFragmentManager().findFragmentById(R.id.gdetail_title);
        title.setTitle(getResources().getString(R.string.goodsdetail));
    }

    private void addCart(){
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("goodsId",this.goodsId);
        param.put("goodsNum", this.number);
        Utils.asyncHttpRequestGet(Constants.URL_ADD_CART, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject res) {
                if(statusCode == 200){
                    JSONObject jo = new JSONObject(res);
                    if(jo.getBoolean("success")){
                        TextView tv = (TextView) findViewById(R.id.gd_cart_number);
                        String num = tv.getText().toString();
                        if(!"".equals(num)){
                            Integer curNumber = Integer.parseInt(num) + number;
                            tv.setText(curNumber.toString());
                        }
                    }else{
                        Toast.makeText(GoodsDetailActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(GoodsDetailActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable t) {
                Toast.makeText(GoodsDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
