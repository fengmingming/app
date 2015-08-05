package com.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
public class ListActivity extends FragmentActivity{
    private IGoodsCom.Config config = new IGoodsCom.Config();
    private int currPage = 1;
    private Button sort_def;
    private Button sort_sales;
    private TextView sort_price;
    private ImageView sort_img;
    private String sortTp;
    private IGoodsCom.ParseJsonToGoods categoryHandler = new IGoodsCom.ParseJsonToGoods() {
        @Override
        public List<GoodsView.Goods> parse(JSONObject json) throws Exception {
            List<GoodsView.Goods> gs = new ArrayList<GoodsView.Goods>();
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
        Title title = (Title)getSupportFragmentManager().findFragmentById(R.id.list_title_fragment);
        String tStr = getIntent().getStringExtra("title");
        title.setTitle(tStr == null?getResources().getString(R.string.list_title):tStr);
        final GoodsFragment gf = (GoodsFragment) getSupportFragmentManager().findFragmentById(R.id.list_goods);
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
        sort_def = (Button) findViewById(R.id.sort_def);
        sort_def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gf.isLoading()){
                    clear();
                    sort_def.setTextColor(getResources().getColor(R.color.def_fontcolor));
                    gf.reset();
                    gf.builder();
                }
            }
        });
        sort_sales = (Button) findViewById(R.id.sort_sales);
        sort_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gf.isLoading()){
                    clear();
                    sort_sales.setTextColor(getResources().getColor(R.color.def_fontcolor));
                    sortTp = "sort_sale";
                    config.getParam().put("sortTp",sortTp);
                    gf.reset();
                    gf.builder();
                }
            }
        });
        sort_price = (TextView) findViewById(R.id.sort_price);
        sort_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gf.isLoading()){
                    if("sort_price_1".equals(sortTp)){
                        sort_img.setImageResource(R.drawable.sort_up);
                        sortTp = "sort_price_0";
                    }else{
                        clear();
                        sort_img.setImageResource(R.drawable.sort_down);
                        sortTp = "sort_price_1";
                    }
                    sort_price.setTextColor(getResources().getColor(R.color.def_fontcolor));
                    config.getParam().put("sortTp",sortTp);
                    gf.reset();
                    gf.builder();
                }
            }
        });
        sort_img = (ImageView) findViewById(R.id.sort_img);
    }

    @Override
    protected void onStart() {
        super.onStart();
        clear();
        sort_def.setTextColor(getResources().getColor(R.color.def_fontcolor));
        GoodsFragment gf = (GoodsFragment) getSupportFragmentManager().findFragmentById(R.id.list_goods);
        config.getParam().put("currPage", currPage = 1);
        gf.reset();
        gf.builder();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void clear(){
        sort_def.setTextColor(getResources().getColor(R.color.black));
        sort_sales.setTextColor(getResources().getColor(R.color.black));
        sort_price.setTextColor(getResources().getColor(R.color.black));
        sortTp = null;
        config.getParam().put("sortTp",null);
        sort_img.setImageResource(0);
    }
}
