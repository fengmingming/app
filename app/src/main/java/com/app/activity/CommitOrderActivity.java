package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.commons.Constants;
import com.app.commons.JSONArray;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by on 2015/7/27.
 */
public class CommitOrderActivity extends Activity {
    private LinearLayout container;
    private TextView remark;
    private TextView receiver;
    private TextView mobile;
    private Button submit;
    private RadioGroup rg;
    private TextView amount;
    private TextView payPrice;
    private TextView discountPrice;
    private String balanceMsg;
    private String payPassHint;
    private boolean flag;
    private boolean isExistAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_order);
        container = (LinearLayout) findViewById(R.id.container);
        LinearLayout address = (LinearLayout) findViewById(R.id.address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CommitOrderActivity.this,AddressActivity.class);
                startActivity(intent);
            }
        });
        remark = (TextView) findViewById(R.id.remark);
        receiver = (TextView) findViewById(R.id.receiver);
        mobile = (TextView) findViewById(R.id.mobile);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        rg = (RadioGroup) findViewById(R.id.group);
        amount = (TextView) findViewById(R.id.amount);
        discountPrice = (TextView) findViewById(R.id.discountPrice);
        payPrice = (TextView) findViewById(R.id.payPrice);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.asyncHttpRequestGet(Constants.URL_CART_AMOUNT, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                if(statusCode == 200){
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        container.removeAllViews();
                        jo = jo.getJSONObject("result");
                        if(jo != null){
                            com.app.commons.JSONObject address = jo.getJSONObject("address");
                            if(address != null){
                                isExistAddress = true;
                                remark.setText(address.getString("addressDetail"));
                                receiver.setText(address.getString("receiver"));
                                mobile.setText(address.getString("mobile"));
                            }
                            com.app.commons.JSONObject cart = jo.getJSONObject("shopCart");
                            if(cart != null){
                                flag = cart.getBoolean("flag");
                                JSONArray ja = cart.getJSONArray("productList");
                                LinearLayout l;
                                for(int i = 0,j=ja.length();i<j;i++){
                                    l = createContainer(ja.getJSONObject(i));
                                    if(l != null){
                                        container.addView(l);
                                    }
                                }
                                amount.setText(cart.getString("amount"));
                                payPrice.setText(cart.getString("payPrice"));
                                discountPrice.setText(cart.getString("discountPrice"));
                            }
                        }
                    }else {
                        Toast.makeText(CommitOrderActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CommitOrderActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private LinearLayout createContainer(com.app.commons.JSONObject jo){
        JSONArray ja = jo.getJSONArray("settleGoods");
        if(ja==null||ja.length()==0){
            return null;
        }
        LinearLayout h = new LinearLayout(CommitOrderActivity.this);
        h.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        h.setOrientation(LinearLayout.HORIZONTAL);
        h.setPadding(5,5,5,0);
        ImageView iv = new ImageView(CommitOrderActivity.this);
        int wh = Math.round(getResources().getDimension(R.dimen.cart_img_wh));
        iv.setLayoutParams(new ViewGroup.LayoutParams(wh,wh));
        LinearLayout v = new LinearLayout(CommitOrderActivity.this);
        LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vlp.setMargins(10,0,0,0);
        vlp.weight = 1;
        vlp.gravity = Gravity.BOTTOM;
        v.setLayoutParams(vlp);
        v.setOrientation(LinearLayout.VERTICAL);
        v.setGravity(Gravity.BOTTOM);
        Utils.asyncLoadInternetImageView(iv, Constants.URL_IMAGE + "/200X200" + ja.getJSONObject(0).getString("photoUrl"));
        TextView goodsName = new TextView(CommitOrderActivity.this);
        if(ja.length() == 1){
            goodsName.setText(ja.getJSONObject(0).getString("goodsName")+"x"+jo.getString("number"));
        }else{
            goodsName.setText(getResources().getString(R.string.goods_set)+"x"+jo.getString("number"));
        }
        TextView price = new TextView(CommitOrderActivity.this);
        price.setText(jo.getString("amount"));
        v.addView(goodsName);
        v.addView(price);
        h.addView(iv);
        h.addView(v);
        return h;
    }

    private void submit(){
        if(!isExistAddress){
            Toast.makeText(CommitOrderActivity.this, "请配置收货地址", Toast.LENGTH_SHORT).show();
            return;
        }
        int payType = 2;
        switch (rg.getCheckedRadioButtonId()){
            case R.id.balance:
                if(balanceMsg != null){
                    Toast.makeText(CommitOrderActivity.this, balanceMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(payPassHint != null){
                    Toast.makeText(CommitOrderActivity.this, payPassHint, Toast.LENGTH_SHORT).show();
                    return;
                }
                payType = 2;
                ;break;
            case R.id.alipay:payType = 1;break;
            case R.id.wx:payType=4;break;
        }

    }
}
