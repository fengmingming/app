package com.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.commons.Constants;
import com.app.view.GoodsView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by on 2015/7/5.
 */
public class GoodsFragment extends Fragment{

    private String url;
    private Map param;

    private final AtomicBoolean loadState = new AtomicBoolean(false);

    public Map getParam() {
        return param;
    }

    public void setParam(Map param) {
        this.param = param;
    }

    private GoodsView goodsList;

    public GoodsFragment(){
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    public boolean getLoadState() {
        return loadState.get();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.goods, container, false);
        this.goodsList = (GoodsView) view.findViewById(R.id.goodsList);
        return view;
    }

    public void builder(){
        if(this.url != null){
            render();
        }
    }

    protected void render(){
        loadState.set(true);
        AsyncHttpClient ac = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        if(param != null){
            for(Object key : param.keySet()){
                if(key != null&&param.get(key)!=null){
                    rp.add(key.toString(),param.get(key).toString());
                }
            }
        }
        ac.post(this.url,rp,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                super.onSuccess(statusCode, headers, res);
                if(statusCode == 200){
                    try{
                        if(res.getBoolean("success")){
                            JSONObject result = res.getJSONObject("result");
                            if(result != null){
                                JSONArray gsList = result.getJSONArray("entry");
                                if(gsList != null&&gsList.length()>0){
                                    List<GoodsView.Goods> gs = new ArrayList<GoodsView.Goods>();
                                    for(int i=0;i<gsList.length();i++){
                                        JSONObject go = gsList.getJSONObject(i);
                                        GoodsView.Goods g = new GoodsView.Goods();
                                        g.setGdurl(Constants.URL_GOODS_DETAIL+go.getString("id"));
                                        g.setGid(go.getLong("id"));
                                        g.setGname(go.getString("goodsName"));
                                        g.setIurl(go.getString("photoUrl"));
                                        g.setRemark(go.getString("remark"));
                                        g.setMprice(go.getString("marketPrice"));
                                        g.setPrice(go.getString("price"));
                                        gs.add(g);
                                    }
                                    goodsList.builder(gs);
                                }
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                loadState.set(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.goodsList != null){
            this.goodsList.removeAllViews();
        }
    }
}
