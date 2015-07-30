package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.MainActivity;
import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by on 2015/7/28.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Title title = (Title)getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.login));
        String clazz = getIntent().getStringExtra("clazz");
        final Intent intent = new Intent();
        if(clazz == null){
            intent.setClass(LoginActivity.this, MainActivity.class);
        }else{
            try{
                intent.setClass(LoginActivity.this, Class.forName(clazz));
            }catch(Exception e){
                intent.setClass(LoginActivity.this, MainActivity.class);
            }
        }
        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);
        final Button btn = (Button) findViewById(R.id.loginBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utils.isValid(username.getText())){
                    username.requestFocus();
                    return;
                }
                if(!Utils.isValid(password.getText())){
                    password.requestFocus();
                    return;
                }
                try{
                    Map<String,Object> param = new HashMap<String, Object>();
                    param.put("mobile",username.getText().toString());
                    param.put("password", Utils.md5(password.getText().toString()));
                    Utils.asyncHttpRequestPost(Constants.URL_LOGIN, param, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                            if (statusCode == 200) {
                                com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                                if (jo.getBoolean("success")) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch(Exception e){
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        final TextView forgetBtn = (TextView) findViewById(R.id.forgetBtn);
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final TextView regBtn = (TextView) findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
