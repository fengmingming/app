package com.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.app.GoodsFragment;
import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.ReScrollView;
import com.app.interfaces.IGoodsCom;
import com.app.view.GoodsView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2015/7/15.
 */
public class ListActivity extends Activity {
    private IGoodsCom.Config config = new IGoodsCom.Config();
    private int currPage = 1;
    private IGoodsCom.ParseJsonToGoods categoryHandler = new IGoodsCom.ParseJsonToGoods() {
        @Override
        public List<GoodsView.Goods> parse(JSONObject json) throws Exception {
            List<GoodsView.Goods> gs = new ArrayList<>();
            if(json.getBoolean("success")){
                json = json.getJSONObject("result");
                if(json != null){
                    JSONArray ja = json.getJSONArray("goodslist");
                    if(ja != null && ja.length() > 0){
                        GoodsView.Goods g;
                        for(int i=0,j=ja.length();i<j;i++){
                            json = ja.getJSONObject(i);
                            g = new GoodsView.Goods();
                            g.setGid(json.getLong("id"));
                            g.setGdurl(Constants.URL_GOODS_DETAIL + g.getGid());
                            g.setGname(json.getString("goodsName"));
                            g.setIurl(Constants.URL_IMAGE + "/200X200" + json.getString("photoUrl"));
                            g.setMprice(json.getString("marketPrice"));
                            g.setPrice(json.getString("price"));
                            g.setRemark(json.getString("remark"));
                            gs.add(g);
                        }
                    }else{
                        Toast.makeText(ListActivity.this, getResources().getString(R.string.no_data_toast_text), Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                Toast.makeText(ListActivity.this, json.getString("errMsg"), Toast.LENGTH_SHORT);
            }
            return gs;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        Title title = (Title)getFragmentManager().findFragmentById(R.id.list_title_fragment);
        title.setTitle(getResources().getString(R.string.list_title));
        final GoodsFragment gf = (GoodsFragment) getFragmentManager().findFragmentById(R.id.list_goods);
        gf.setConfig(config);
        config.setUrl(getIntent().getStringExtra("url"));
        if(config.getUrl().startsWith(Constants.URL_CATEGORY_GOODS_LIST)){
            gf.parseJsonToGoods(categoryHandler);
        }
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout)findViewById(R.id.list_swipe);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                gf.reset();
                config.getParam().put("currPage", currPage = 1);
                gf.builder();
            }
        });
        final ReScrollView sv = (ReScrollView) findViewById(R.id.list_scroll);
        sv.setScrollChangeEvent(new ReScrollView.ScrollChangeEvent() {
            @Override
            public void onScrollChanged() {
                config.getParam().put("currPage", ++currPage);
                gf.builder();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoodsFragment gf = (GoodsFragment) getFragmentManager().findFragmentById(R.id.list_goods);
        config.getParam().put("currPage", currPage = 1);
        gf.builder();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoodsFragment gf = (GoodsFragment) getFragmentManager().findFragmentById(R.id.list_goods);
        gf.reset();
    }
}
