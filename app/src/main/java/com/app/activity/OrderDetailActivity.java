package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developserver on 2015/8/1.
 */
public class OrderDetailActivity extends FragmentActivity {
    private LinearLayout container;
    private TextView orderNum;
    private TextView state;
    private TextView remark;
    private TextView receiver;
    private TextView payType;
    private TextView dtype;
    private long id;
    private TextView orderPrice;
    private Button payBtn;
    private Button delBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odetail);
        Title title = (Title) getSupportFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.title_orders_detail));
        container = (LinearLayout)findViewById(R.id.container);
        orderNum = (TextView)findViewById(R.id.orderNum);
        state = (TextView)findViewById(R.id.state);
        remark = (TextView)findViewById(R.id.remark);
        receiver = (TextView)findViewById(R.id.receiver);
        payType = (TextView)findViewById(R.id.payType);
        dtype = (TextView)findViewById(R.id.dtype);
        id = getIntent().getLongExtra("id",0);
        orderPrice = (TextView)findViewById(R.id.orderPrice);
        payBtn = (Button)findViewById(R.id.payBtn);
        delBtn = (Button)findViewById(R.id.delBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        flush();
    }

    private void flush(){
        if(id <= 0){
            return;
        }
        Map<String,Object> param = new HashMap<>();
        param.put("orderId",id);
        Utils.asyncHttpRequestGet(Constants.URL_ODETAIL, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                if(statusCode == 200){
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        jo = jo.getJSONObject("result");
                        if(jo != null){
                            container.removeAllViews();
                            payBtn.setVisibility(View.INVISIBLE);
                            delBtn.setVisibility(View.INVISIBLE);
                            orderNum.setText(jo.getString("orderNum"));
                            if(jo.getInt("state") != 1){
                                state.setText(Utils.createState(jo.getInt("state")));
                            }else if(jo.getInt("isPaid") == 1){
                                state.setText("未付款");
                            }else{
                                state.setText(Utils.createStatus(jo.getInt("status")));
                            }
                            if(jo.getInt("state") == 1){
                                if(jo.getInt("isPaid") == 1){
                                    final int payType = jo.getInt("payCode");
                                    if(payType != 2){
                                        payBtn.setVisibility(View.VISIBLE);
                                        payBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Utils.pay(OrderDetailActivity.this, payType);
                                            }
                                        });
                                    }
                                }
                                if(jo.getInt("status") < 3){
                                    delBtn.setVisibility(View.VISIBLE);
                                    delBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Map<String,Object> param = new HashMap<String, Object>();
                                            param.put("orderId", id);
                                            Utils.asyncHttpRequestGet(Constants.URL_ODETELE, param,new JsonHttpResponseHandler(){
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                                                    if(statusCode == 200){
                                                        com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                                                        if(jo.getBoolean("success")){
                                                            flush();
                                                        }else{
                                                            Toast.makeText(OrderDetailActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }else{
                                                        Toast.makeText(OrderDetailActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                            remark.setText(jo.getString("remark"));
                            receiver.setText(jo.getString("receiver")+"  "+jo.getString("mobile"));
                            dtype.setText(jo.getInt("deliveryType")==1?"到亭自提":"送货上门");
                            payType.setText(jo.getString("payName"));
                            orderPrice.setText(jo.getString("orderPrice"));
                            JSONArray ja = jo.getJSONArray("orderDetailList");
                            if(ja!=null&&ja.length()>0){
                                for(int i=0,j=ja.length();i<j;i++){
                                    container.addView(createGoods(ja.getJSONObject(i)));
                                }
                            }
                        }
                    }else{
                        Toast.makeText(OrderDetailActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OrderDetailActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private LinearLayout createGoods(com.app.commons.JSONObject jo){
        int wh = Math.round(getResources().getDimension(R.dimen.goods_img_wh) / 2);
        LinearLayout v = new LinearLayout(OrderDetailActivity.this);
        v.setPadding(5,5,5,5);
        v.setOrientation(LinearLayout.HORIZONTAL);
        v.setPadding(5,5,5,5);
        ImageView iv = new ImageView(OrderDetailActivity.this);
        iv.setLayoutParams(new ViewGroup.LayoutParams(wh,wh));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        Utils.asyncLoadInternetImageView(iv,Constants.URL_IMAGE+"/200X200"+jo.getString("photoUrl"));
        v.addView(iv);
        TextView goodsName = new TextView(OrderDetailActivity.this);
        goodsName.setPadding(10,5,5,5);
        goodsName.setText(jo.getString("goodsName")+"x"+jo.getString("number"));
        v.addView(goodsName);
        final long gid = jo.getLong("goodsId");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(OrderDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra("url", Constants.URL_GOODS_DETAIL + gid);
                startActivity(intent);
            }
        });
        return v;
    }
}
