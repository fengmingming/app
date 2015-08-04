package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.MainActivity;
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
 * Created by developserver on 2015/7/28.
 */
public class RegisterActivity extends Activity {
    private EditText username;
    private EditText password;
    private EditText mobile;
    private EditText mobileValidCode;
    private EditText imgCode;
    private EditText password2;
    private Button reqMobileValid;
    private ImageButton imgCodeBtn;
    private Button regBtn;
    private View.OnFocusChangeListener vof = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            EditText et = (EditText) view;
            if(!b){
                if(et.getText().length() == 0){
                    et.setError(getResources().getString(R.string.no_blank));
                }else if(et.getText().length() < 6||et.getText().length() > 20){
                    et.setError("请输入6-20位数字,字母");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.register));
        username = (EditText) findViewById(R.id.username);
        username.setOnFocusChangeListener(vof);
        password = (EditText) findViewById(R.id.password);
        password.setOnFocusChangeListener(vof);
        password2 = (EditText) findViewById(R.id.password2);
        password2.setOnFocusChangeListener(vof);
        mobile = (EditText) findViewById(R.id.mobile);
        mobileValidCode = (EditText) findViewById(R.id.reg_valid_code);
        imgCode = (EditText) findViewById(R.id.reg_img_code);
        reqMobileValid = (Button) findViewById(R.id.reg_valid_code_btn);
        reqMobileValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMobileCode();
            }
        });
        imgCodeBtn = (ImageButton) findViewById(R.id.reg_img_code_btn);
        imgCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.asyncLoadInternetImageView(imgCodeBtn, Utils.wrapUrl(Constants.URL_REQIMGCODE + "?num=" + Math.random()));
            }
        });
        regBtn = (Button) findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void sendMobileCode(){
        String mobile = this.mobile.getText().toString();
        if(mobile.matches("\\d{11}")){
            Map<String,Object> param = new HashMap<>();
            param.put("mobile",mobile);
            Utils.asyncHttpRequestPost(Constants.URL_SENDMOBILECODE, param, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                    if(statusCode == 200){
                        com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                        if(jo.getBoolean("success")){
                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.suc_send_mobile_code), Toast.LENGTH_SHORT).show();
                            reqMobileValid.setClickable(false);
                            reqMobileValid.setBackgroundColor(getResources().getColor(R.color.gray));
                            reqMobileValid.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    reqMobileValid.setClickable(true);
                                    reqMobileValid.setBackgroundColor(getResources().getColor(R.color.orange));
                                }
                            },60000);
                        }else{
                            Toast.makeText(RegisterActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void register(){
        String username = this.username.getText().toString();
        if(username.length() < 6 || username.length() > 20){
            this.username.requestFocus();
            return;
        }
        String password = this.password.getText().toString();
        if(password.length() < 6 || password.length() > 20){
            this.password.requestFocus();
            return;
        }
        String password2 = this.password2.getText().toString();
        if(!password2.equals(password)){
            Toast.makeText(RegisterActivity.this, "两次密码输入不一致",Toast.LENGTH_SHORT);
            return;
        }
        String imgCode = this.imgCode.getText().toString();
        if(!Utils.isValid(imgCode)){
            this.imgCode.setError("验证码必填");
            this.imgCode.requestFocus();
            return;
        }
        String mobile = this.mobile.getText().toString();
        String mobileCode = this.mobileValidCode.getText().toString();
        if(Utils.isValid(mobile)){
            if(!Utils.isValid(mobileCode)){
                this.mobileValidCode.setError("手机验证码必填");
                this.mobileValidCode.requestFocus();
                return;
            }
        }
        Map<String,Object> param = new HashMap<>();
        param.put("userName",username);
        param.put("password",Utils.md5(password));
        if(Utils.isValid(mobile)){
            param.put("mobile",mobile);
            param.put("mobileCode",mobileCode);
        }
        param.put("imgCode", imgCode);
        Utils.asyncHttpRequestPost(Constants.URL_REGISTER, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                if(statusCode == 200){
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.asyncLoadInternetImageView(imgCodeBtn, Constants.URL_REQIMGCODE + "?num=" + Math.random());
    }
}
