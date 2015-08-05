package com.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.R;
import com.app.commons.ComboxDto;
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
 * Created by developserver on 2015/7/30.
 */
public class AddAddressActivity extends FragmentActivity {
    private Long addressId;
    private Spinner province;
    private Spinner city;
    private Spinner district;
    private Spinner community;
    private Spinner pavilion;
    private EditText remark;
    private EditText mobile;
    private EditText receiver;
    private class Handler extends JsonHttpResponseHandler{
        private Spinner spin;
        public Handler(Spinner spin){
            this.spin = spin;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
            if(statusCode == 200){
                com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                if(jo.getBoolean("success")){
                    JSONArray ja = jo.getJSONArray("result");
                    if(ja != null){
                        List<ComboxDto> list = new ArrayList<>();
                        for(int i=0,j=ja.length();i<j;i++){
                            list.add(new ComboxDto(ja.getJSONObject(i).getString("k"),ja.getJSONObject(i).getString("v")));
                        }
                        spin.setAdapter(new ArrayAdapter<ComboxDto>(AddAddressActivity.this, android.R.layout.simple_spinner_item,list.toArray(new ComboxDto[]{})));
                    }
                }else{
                    Toast.makeText(AddAddressActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(AddAddressActivity.this, statusCode, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        if(getIntent().getLongExtra("addressId",0) > 0){
            addressId = getIntent().getLongExtra("addressId",0);
        }
        province = (Spinner) findViewById(R.id.province);
        province.setAdapter(new ArrayAdapter<ComboxDto>(AddAddressActivity.this, android.R.layout.simple_spinner_item, new ComboxDto[]{new ComboxDto("北京","2")}));
        province.setSelection(0);
        city = (Spinner) findViewById(R.id.city);
        city.setAdapter(new ArrayAdapter<ComboxDto>(AddAddressActivity.this, android.R.layout.simple_spinner_item, new ComboxDto[]{new ComboxDto("北京市","52")}));
        city.setSelection(0);
        district = (Spinner) findViewById(R.id.district);
        Map<String,Object> param = new HashMap<>();
        param.put("pid",52);
        Utils.asyncHttpRequestGet(Constants.URL_AREA, param, new Handler(district));
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ComboxDto dto = (ComboxDto)adapterView.getSelectedItem();
                Map<String,Object> param = new HashMap<>();
                param.put("pid",dto.getValue());
                Utils.asyncHttpRequestGet(Constants.URL_AREA, param, new Handler(community));
                pavilion.setAdapter(new ArrayAdapter<ComboxDto>(AddAddressActivity.this, android.R.layout.simple_spinner_item,new ComboxDto[]{}));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        community = (Spinner) findViewById(R.id.community);
        community.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ComboxDto dto = (ComboxDto)adapterView.getSelectedItem();
                Map<String,Object> param = new HashMap<>();
                param.put("pid",dto.getValue());
                Utils.asyncHttpRequestGet(Constants.URL_AREA, param, new Handler(pavilion));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        pavilion = (Spinner) findViewById(R.id.pavilion);
        remark = (EditText) findViewById(R.id.remark);
        mobile = (EditText) findViewById(R.id.mobile);
        receiver = (EditText) findViewById(R.id.receiver);
        Button btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> param = new HashMap<String, Object>();
                if(province.getSelectedItem() == null){
                    Toast.makeText(AddAddressActivity.this, "省不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("province",((ComboxDto)province.getSelectedItem()).getValue());
                if(city.getSelectedItem() == null){
                    Toast.makeText(AddAddressActivity.this, "市不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("city",((ComboxDto)city.getSelectedItem()).getValue());
                if(district.getSelectedItem() == null){
                    Toast.makeText(AddAddressActivity.this, "区不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("district",((ComboxDto)district.getSelectedItem()).getValue());
                if(community.getSelectedItem() == null){
                    Toast.makeText(AddAddressActivity.this, "商圈不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("community",((ComboxDto)community.getSelectedItem()).getValue());
                if(pavilion.getSelectedItem() == null){
                    Toast.makeText(AddAddressActivity.this, "亭子不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("pavilionId",((ComboxDto)pavilion.getSelectedItem()).getValue());
                if(addressId != null){
                    param.put("id", addressId);
                }
                if("".equals(remark.getText().toString().trim())){
                    Toast.makeText(AddAddressActivity.this, "详细地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("addressDetail",remark.getText().toString().trim());
                if("".equals(receiver.getText().toString().trim())){
                    Toast.makeText(AddAddressActivity.this, "收货人不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("receiver",receiver.getText().toString().trim());
                if("".equals(mobile.getText().toString().trim())){
                    Toast.makeText(AddAddressActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!mobile.getText().toString().matches("^1[0-9]{10}")){
                    Toast.makeText(AddAddressActivity.this, "手机号格式不对", Toast.LENGTH_SHORT).show();
                    return;
                }
                param.put("mobile",mobile.getText().toString().trim());
                Utils.asyncHttpRequestPost(Constants.URL_ADDORUPDAREA,param,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if(statusCode == 200){
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            if(jo.getBoolean("success")){
                                AddAddressActivity.this.onBackPressed();
                            }else{
                                Toast.makeText(AddAddressActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT);
                            }
                        }else{
                            Toast.makeText(AddAddressActivity.this,statusCode,Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(addressId != null){
            Map param = new HashMap();
            param.put("addressId",addressId);
            Utils.asyncHttpRequestGet(Constants.URL_ADDRESS_ID,param,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                    if(statusCode == 200){
                        com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                        if(jo.getBoolean("success")){
                            jo = jo.getJSONObject("result");
                            if(jo != null){
                                receiver.setText(jo.getString("receiver"));
                                mobile.setText(jo.getString("mobile"));
                                remark.setText(jo.getString("addressDetail"));
                            }
                        }else{
                            Toast.makeText(AddAddressActivity.this,jo.getString("errMsg"),Toast.LENGTH_SHORT);
                        }
                    }else{
                        Toast.makeText(AddAddressActivity.this,statusCode,Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }
}
