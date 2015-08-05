package com.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.commons.Constants;
import com.app.commons.Utils;
import com.app.interfaces.IGoodsCom;
import com.app.view.GoodsView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by on 2015/7/5.
 */
public class GoodsFragment extends Fragment implements IGoodsCom{

    private final AtomicBoolean loadState = new AtomicBoolean(false);

    private GoodsView gv;

    private Config config;

    private ParseJsonToGoods handler;

    public GoodsFragment(){
    }

    @Override
    public void parseJsonToGoods(ParseJsonToGoods parseJsonToGoods){
        this.handler = parseJsonToGoods;
    }

    @Override
    public boolean isLoading() {
        return loadState.get();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.goods, container, false);
        this.gv = (GoodsView) view.findViewById(R.id.goodsList);
        return view;
    }

    @Override
    public void builder(){
        if(config != null && !loadState.get()){
            render();
        }
    }

    private void render(){
        loadState.set(true);
        Utils.asyncHttpRequestPost(config.getUrl(), config.getParam(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                super.onSuccess(statusCode, headers, res);
                if(statusCode == 200){
                    try{
                        if(handler != null){
                            List<GoodsView.Goods> gs = handler.parse(res);
                            if(gs != null && gs.size() > 0){
                                gv.builder(gs);
                            }else{
                                delayedChangeState(2000);
                                return ;
                            }
                        }
                    }catch(Exception e){
                        Log.e(GoodsFragment.class.getName(),e.getMessage(),e);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "网络异常:" + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
                }
                delayedChangeState(1000);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse){
                loadState.set(false);
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void delayedChangeState(long mills){
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadState.set(false);
            }
        }, mills);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reset();
    }

    @Override
    public void reset(){
        if(this.gv != null){
            this.gv.removeAllViews();
        }
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
