package com.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.GoodsFragment;
import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.JSONArray;
import com.app.commons.ReScrollView;
import com.app.interfaces.IGoodsCom;
import com.app.view.GoodsView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developserver on 2015/7/23.
 */
public class SearchActivity extends Activity {
    private int currPage = 1;
    private GoodsFragment gf;
    private EditText text;
    private TextView tip;
    private IGoodsCom.Config config = new IGoodsCom.Config();
    private IGoodsCom.ParseJsonToGoods handler = new IGoodsCom.ParseJsonToGoods() {
        @Override
        public List<GoodsView.Goods> parse(JSONObject jsonObject) throws Exception {
            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(jsonObject);
            List<GoodsView.Goods> list = new ArrayList<>();
            if(jo.getBoolean("success")){
                JSONArray ja = jo.getJSONArray("result");
                if(ja != null&&ja.length() > 0){
                    currPage = currPage + 1;
                    com.app.commons.JSONObject jo2;
                    for(int i=0,j=ja.length();i<j;i++){
                        jo2 = ja.getJSONObject(i);
                        if(jo2 != null){
                            GoodsView.Goods goods = new GoodsView.Goods();
                            goods.setMprice(jo2.getString("marketPrice"));
                            goods.setRemark(jo2.getString("remark"));
                            goods.setIurl(Constants.URL_IMAGE + "/200X200" +jo2.getString("photoUrl"));
                            goods.setPrice(jo2.getString("price"));
                            goods.setGname(jo2.getString("goodsName"));
                            goods.setGid(jo2.getLong("id"));
                            goods.setGdurl(Constants.URL_GOODS_DETAIL + jo2.getString("id"));
                            list.add(goods);
                        }
                    }
                }else{
                    if(currPage == 1){
                        tip.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                Toast.makeText(SearchActivity.this, jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
            }
            return list;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        gf = (GoodsFragment)getFragmentManager().findFragmentById(R.id.container);
        gf.parseJsonToGoods(handler);
        gf.setConfig(config);
        config.setUrl(Constants.URL_SEARCH);
        text = (EditText) findViewById(R.id.search);
        tip = (TextView) findViewById(R.id.tip);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.search));
        final TextView et = (TextView) findViewById(R.id.sbtn);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        final ReScrollView sv = (ReScrollView) findViewById(R.id.scrollView);
        sv.setScrollChangeEvent(new ReScrollView.ScrollChangeEvent() {
            @Override
            public void onScrollChanged() {
                if(currPage > 1){
                    config.getParam().put("currPage", currPage);
                    gf.builder();
                }
            }
        });
    }

    private void search(){
        String str = text.getText().toString().trim();
        if(!"".equals(str)){
            tip.setVisibility(View.GONE);
            gf.reset();
            config.getParam().put("currPage", currPage = 1);
            config.getParam().put("content", str);
            gf.builder();
        }
    }
}
