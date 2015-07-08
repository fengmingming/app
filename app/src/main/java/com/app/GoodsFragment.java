package com.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.Map;

/**
 * Created by on 2015/7/5.
 */
public class GoodsFragment extends Fragment{

    private String url;
    private Map param;

    public Map getParam() {
        return param;
    }

    public void setParam(Map param) {
        this.param = param;
    }

    private LinearLayout goodsList;

    public GoodsFragment(){
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.goods,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(this.url != null){
            this.goodsList = (LinearLayout)getActivity().findViewById(R.id.goodsList);
        }
    }

    protected void render(){
        AsyncHttpClient ac = new AsyncHttpClient();
        RequestParams rp = new RequestParams();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.goodsList.removeAllViews();
    }
}
