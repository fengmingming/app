package com.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by developserver on 2015/8/3.
 */
public class ResetPassActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpass);
        Title title = (Title)getSupportFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.resetpass));
        final EditText m = (EditText) findViewById(R.id.mobile);
        final EditText password = (EditText) findViewById(R.id.password);
        final Button sm = (Button) findViewById(R.id.sm);
        sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence mobile = m.getText();
                if(mobile==null||!mobile.toString().trim().matches("")){
                    Toast.makeText(ResetPassActivity.this, getResources().getString(R.string.mobile_regex_err), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("mobile",mobile.toString().trim());
                Utils.asyncHttpRequestPost(Constants.URL_SENDMOBILECODE, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if (statusCode == 200) {
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if (jo.getBoolean("success")) {
                                Toast.makeText(ResetPassActivity.this, getResources().getString(R.string.suc_send_mobile_code), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPassActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ResetPassActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        final EditText codeT = (EditText) findViewById(R.id.code);
        Button btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = m.getText().toString().trim();
                String code = codeT.getText().toString().trim();
                String pass = Utils.md5(password.getText().toString().trim());
                if("".equals(mobile)){
                    Toast.makeText(ResetPassActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(code)){
                    Toast.makeText(ResetPassActivity.this, "手机验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(pass)){
                    Toast.makeText(ResetPassActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> param = new HashMap<String, Object>();
                Utils.asyncHttpRequestPost(Constants.URL_RESETPASS,param,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if (statusCode == 200) {
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if (jo.getBoolean("success")) {
                                Toast.makeText(ResetPassActivity.this, getResources().getString(R.string.pass_up_suc), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPassActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ResetPassActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
