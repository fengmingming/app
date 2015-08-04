package com.app.activity;

import android.app.Activity;
import android.media.Rating;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.JSONArray;
import com.app.commons.ReScrollView;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developserver on 2015/8/1.
 */
public class CouponActivity extends Activity {
    private int curBtn = 1;
    private int curPage = 0;
    private int rows = 10;
    private Button nouse;
    private Button used;
    private Button invalid;
    private EditText couponCode;
    private Button couponBtn;
    private ReScrollView scrollView;
    private LinearLayout container;
    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon);
        Title title = (Title)getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.coupon));
        nouse = (Button) findViewById(R.id.nouse);
        nouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curBtn = 1;
                clear();
                nouse.setTextColor(getResources().getColor(R.color.def_fontcolor));
                flush();
            }
        });
        used = (Button) findViewById(R.id.used);
        used.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curBtn = 2;
                clear();
                used.setTextColor(getResources().getColor(R.color.def_fontcolor));
                flush();
            }
        });
        invalid = (Button) findViewById(R.id.invalid);
        invalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curBtn = 3;
                clear();
                invalid.setTextColor(getResources().getColor(R.color.def_fontcolor));
                flush();
            }
        });
        couponBtn = (Button) findViewById(R.id.couponBtn);
        couponCode = (EditText) findViewById(R.id.couponCode);
        container = (LinearLayout) findViewById(R.id.container);
        scrollView = (ReScrollView) findViewById(R.id.scrollView);
        scrollView.setScrollChangeEvent(new ReScrollView.ScrollChangeEvent() {
            @Override
            public void onScrollChanged() {
                flush();
            }
        });
        nouse.setTextColor(getResources().getColor(R.color.def_fontcolor));
        couponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = couponCode.getText().toString().trim();
                if(!"".equals(code)){
                    Map<String,Object> param = new HashMap<String, Object>();
                    param.put("code",code);
                    Utils.asyncHttpRequestPost(Constants.URL_COUPONCODE,param,new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                            if(statusCode == 200){
                                com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                                if(jo.getBoolean("success")){
                                    Toast.makeText(CouponActivity.this, "领取成功",Toast.LENGTH_SHORT).show();
                                    curBtn = 1;
                                    clear();
                                    nouse.setTextColor(getResources().getColor(R.color.def_fontcolor));
                                    flush();
                                }else{
                                    Toast.makeText(CouponActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(CouponActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        curBtn = 1;
        clear();
        nouse.setTextColor(getResources().getColor(R.color.def_fontcolor));
        flush();
    }

    private void clear(){
        curPage = 0;
        nouse.setTextColor(getResources().getColor(R.color.black));
        used.setTextColor(getResources().getColor(R.color.black));
        invalid.setTextColor(getResources().getColor(R.color.black));
        container.removeAllViews();
    }
    private void flush(){
        if(isLoading){
            return;
        }
        curPage ++;
        isLoading = true;
        Map<String,Object> param = new HashMap<>();
        param.put("type", curBtn);
        param.put("page",curPage);
        Utils.asyncHttpRequestGet(Constants.URL_MYCOUPON,param,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                if (statusCode == 200){
                    isLoading = false;
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        jo = jo.getJSONObject("result");
                        if(jo != null){
                            JSONArray ja = jo.getJSONArray("entry");
                            if(ja!=null&&ja.length()>0){
                                for(int i =0,j=ja.length();i<j;i++){
                                    container.addView(createCoupon(ja.getJSONObject(i)));
                                }
                            }else{
                                Toast.makeText(CouponActivity.this, getResources().getString(R.string.no_data_toast_text), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(CouponActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CouponActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private LinearLayout createCoupon(final com.app.commons.JSONObject jo){
        View view = this.getLayoutInflater().inflate(R.layout.coupon_f,null);
        CheckBox cb = (CheckBox)view.findViewById(R.id.checkbox);
        cb.setVisibility(View.GONE);
        TextView money = (TextView)view.findViewById(R.id.money);
        money.setText("￥"+jo.getString("parValue"));
        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(jo.getString("name"));
        TextView daterange = (TextView)view.findViewById(R.id.daterange);
        daterange.setText(jo.getString("validityStart")+" 至 "+jo.getString("validityEnd"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,10,5,0);
        LinearLayout re = (LinearLayout)view.findViewById(R.id.container);
        re.setLayoutParams(lp);
        return re;
    }
}
