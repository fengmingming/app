package com.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2015/8/2.
 */
public class SecurityActivity extends Activity {
    private EditText mobile;
    private EditText payPass;
    private EditText code;
    private EditText code2;
    private Button sm;
    private Button sm2;
    private Button save;
    private Button save2;
    private Button bindMobile;
    private Button setPayPass;
    private LinearLayout bmContainer;
    private LinearLayout ppContainer;
    private boolean isExistMobile = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.security));
        mobile = (EditText) findViewById(R.id.mobile);
        payPass = (EditText) findViewById(R.id.payPass);
        code = (EditText) findViewById(R.id.code);
        code2 = (EditText) findViewById(R.id.code2);
        sm = (Button) findViewById(R.id.sm);
        sm2 = (Button) findViewById(R.id.sm2);
        save = (Button) findViewById(R.id.save);
        save2 = (Button) findViewById(R.id.save2);
        bindMobile = (Button) findViewById(R.id.bindMobile);
        bindMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExistMobile){
                    Toast.makeText(SecurityActivity.this,getResources().getString(R.string.bindmobile_err),Toast.LENGTH_SHORT).show();
                    return;
                }
                clear();
                bindMobile.setTextColor(getResources().getColor(R.color.def_fontcolor));
                bmContainer.setVisibility(View.VISIBLE);
            }
        });
        setPayPass = (Button) findViewById(R.id.setPayPass);
        setPayPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                setPayPass.setTextColor(getResources().getColor(R.color.def_fontcolor));
                ppContainer.setVisibility(View.VISIBLE);
            }
        });
        bmContainer = (LinearLayout) findViewById(R.id.bmContainer);
        ppContainer = (LinearLayout) findViewById(R.id.ppContainer);
        sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence mobile = SecurityActivity.this.mobile.getText();
                if(mobile==null||!mobile.toString().trim().matches("^1[0-9]{10}")){
                    Toast.makeText(SecurityActivity.this,getResources().getString(R.string.mobile_regex_err),Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("mobile",mobile.toString().trim());
                Utils.asyncHttpRequestPost(Constants.URL_SENDMOBILECODE,param,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if(statusCode == 200){
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if(jo.getBoolean("success")){
                                sm.setClickable(false);
                                sm.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sm.setClickable(true);
                                    }
                                },1000*60);
                                Toast.makeText(SecurityActivity.this,getResources().getString(R.string.suc_send_mobile_code),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SecurityActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SecurityActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        sm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExistMobile){
                    Toast.makeText(SecurityActivity.this, getResources().getString(R.string.no_mobile),Toast.LENGTH_SHORT).show();
                    return;
                }
                Utils.asyncHttpRequestPost(Constants.URL_SENDMCTOUM,null,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if(statusCode == 200){
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if(jo.getBoolean("success")){
                                sm2.setClickable(false);
                                sm2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sm2.setClickable(true);
                                    }
                                },1000*60);
                                Toast.makeText(SecurityActivity.this,getResources().getString(R.string.suc_send_mobile_code),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SecurityActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SecurityActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence mobile = SecurityActivity.this.mobile.getText();
                if(mobile==null||!mobile.toString().trim().matches("^1[0-9]{10}$")){
                    Toast.makeText(SecurityActivity.this,getResources().getString(R.string.mobile_regex_err),Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(code.getText().toString().trim())){
                    Toast.makeText(SecurityActivity.this,getResources().getString(R.string.no_validcode),Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("mobile",mobile.toString().trim());
                param.put("code", code.getText().toString());
                Utils.asyncHttpRequestPost(Constants.URL_BINGDMOBILE,param,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if(statusCode == 200){
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if(jo.getBoolean("success")){
                                Toast.makeText(SecurityActivity.this,"成功绑定手机号",Toast.LENGTH_SHORT).show();
                                isExistMobile = true;
                            }else{
                                Toast.makeText(SecurityActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SecurityActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExistMobile){
                    Toast.makeText(SecurityActivity.this, getResources().getString(R.string.no_mobile),Toast.LENGTH_SHORT).show();
                    return;
                }
                String pass = payPass.getText().toString();
                if(pass.length() < 6||pass.length() > 11){
                    Toast.makeText(SecurityActivity.this,getResources().getString(R.string.pass_regex_err),Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(code2.getText().toString().trim())){
                    Toast.makeText(SecurityActivity.this,getResources().getString(R.string.no_validcode),Toast.LENGTH_SHORT).show();
                    return;
                }
                pass = Utils.md5(pass.trim());
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("pass",pass);
                param.put("code", code2.getText().toString());
                Utils.asyncHttpRequestPost(Constants.URL_PAYPASS,param,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if(statusCode == 200){
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if(jo.getBoolean("success")){
                                Toast.makeText(SecurityActivity.this,"支付密码设置成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SecurityActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SecurityActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        clear();
        setPayPass.setTextColor(getResources().getColor(R.color.def_fontcolor));
        ppContainer.setVisibility(View.VISIBLE);
    }

    private void clear(){
        bindMobile.setTextColor(getResources().getColor(R.color.black));
        setPayPass.setTextColor(getResources().getColor(R.color.black));
        bmContainer.setVisibility(View.GONE);
        ppContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.asyncHttpRequestGet(Constants.URL_ISEXISTMOBILE,null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                if(statusCode == 200){
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        if(!jo.getBoolean("result")){
                            isExistMobile = false;
                        }
                    }else{
                        Toast.makeText(SecurityActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SecurityActivity.this,statusCode,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
