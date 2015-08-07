package com.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.activity.CartActivtiy;
import com.app.activity.CategoryActivity;
import com.app.activity.LoginActivity;
import com.app.activity.MyCenterActivity;
import com.app.activity.SearchActivity;
import com.app.adapter.LoopImgsAdapter;
import com.app.commons.Constants;
import com.app.commons.ReScrollView;
import com.app.commons.Utils;
import com.app.interfaces.IGoodsCom;
import com.app.view.GoodsView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private volatile int currPage = 1;
    private IGoodsCom.Config config = new IGoodsCom.Config();
    public MainActivityFragment() {
        config.setUrl(Constants.URL_INDEX_FLOOR);
        config.getParam().put("num", 10);
        config.getParam().put("type", 0);
        config.getParam().put("currPage", currPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container, container, false);
        //分页加载
        final GoodsFragment gf = (GoodsFragment)getChildFragmentManager().findFragmentById(R.id.index_goods);
        gf.setConfig(config);
        gf.parseJsonToGoods(new IGoodsCom.ParseJsonToGoods() {
            @Override
            public List<GoodsView.Goods> parse(JSONObject res) throws Exception{
                JSONObject result = res.getJSONObject("result");
                List<GoodsView.Goods> gs = new ArrayList<GoodsView.Goods>();
                if(result != null){
                    JSONArray gsList = result.getJSONArray("entry");
                    if(gsList != null&&gsList.length()>0){
                        for(int i=0;i<gsList.length();i++){
                            JSONObject go = gsList.getJSONObject(i);
                            GoodsView.Goods g = new GoodsView.Goods();
                            g.setGdurl(Constants.URL_GOODS_DETAIL + go.getString("id"));
                            g.setGid(go.getLong("id"));
                            g.setGname(go.getString("goodsName"));
                            g.setIurl(Constants.URL_IMAGE + "/200X200" + go.getString("photoUrl"));
                            g.setRemark(go.getString("remark"));
                            g.setMprice(go.getString("marketPrice"));
                            g.setPrice(go.getString("price"));
                            gs.add(g);
                        }
                    }else{
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_data_toast_text), Toast.LENGTH_SHORT).show();
                    }
                }
                return gs;
            }
        });
        ReScrollView sv = (ReScrollView)view.findViewById(R.id.index_scroll);
        sv.setScrollChangeEvent(new ReScrollView.ScrollChangeEvent() {
            @Override
            public void onScrollChanged() {
                config.getParam().put("currPage", ++currPage);
                gf.builder();
            }
        });

        //轮播图
        final ViewPager vp = (ViewPager)view.findViewById(R.id.index_loops);
        ImageView iv = new ImageView(getActivity());
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        iv.setImageResource(R.drawable.default_viwepager);
        List<ImageView> list = new ArrayList<ImageView>();
        list.add(iv);
        vp.setAdapter(new LoopImgsAdapter(list));
        broadcastImages(vp);
        //下拉刷新
        final SwipeRefreshLayout flush = (SwipeRefreshLayout)view.findViewById(R.id.index_swipe_reflush);
        flush.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vp.removeAllViews();
                broadcastImages(vp);
                gf.reset();
                config.getParam().put("currPage", currPage = 1);
                gf.builder();
                flush.setRefreshing(false);
            }
        });

        //事件处理

        ImageButton search = (ImageButton)view.findViewById(R.id.index_search_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        ImageButton category = (ImageButton)view.findViewById(R.id.index_category_btn);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        ImageButton cart = (ImageButton) view.findViewById(R.id.index_cart_btn);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivityFragment.this.getActivity(), CartActivtiy.class);
                MainActivityFragment.this.getActivity().startActivity(intent);
            }
        });
        ImageButton mycenter = (ImageButton) view.findViewById(R.id.index_mycenter_btn);
        mycenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.asyncHttpRequestGet(Constants.URL_ISNOTLOGIN, null, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if(statusCode == 200){
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            Intent intent = new Intent();
                            if(jo.getBoolean("success")){
                                intent.setClass(MainActivityFragment.this.getActivity(), MyCenterActivity.class);
                            }else{
                                intent.setClass(MainActivityFragment.this.getActivity(), LoginActivity.class);
                                intent.putExtra("clazz", MyCenterActivity.class.getName());
                            }
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivityFragment.this.getActivity(), statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        gf.builder();
        return view;
    }

    private void broadcastImages(final ViewPager vp){
        Utils.asyncHttpRequestGet(Constants.URL_INDEX_LOOP, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                super.onSuccess(statusCode, headers, res);
                try{
                    if(statusCode == 200&&res.getBoolean("success")){
                        JSONArray list = res.getJSONArray("result");
                        if(list.length() > 0){
                            List<ImageView> imgs = new ArrayList<ImageView>(list.length());
                            for(int i=0,j=list.length();i<j;i++) {
                                JSONObject jo = list.getJSONObject(i);
                                ImageView iv = new ImageView(getActivity());
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                imgs.add(iv);
                                Utils.asyncLoadInternetImageView(iv,Constants.URL_IMAGE+jo.getString("imgUrl"));
                                String url = jo.getString("url");
                                if(url != null){
                                    iv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }
                            }
                            vp.setAdapter(new LoopImgsAdapter(imgs));
                        }
                    }
                }catch(Exception e){
                    Log.e(MainActivityFragment.class.getName(),e.getMessage(),e);
                }
            }
        });
    }

}
