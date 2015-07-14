package com.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.app.activity.WebActivity;
import com.app.adapter.LoopImgsAdapter;
import com.app.commons.Constants;
import com.app.commons.ReScrollView;
import com.app.commons.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private volatile int currPage = 1;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container, container, false);
        //分页加载
        final GoodsFragment gf = (GoodsFragment)getFragmentManager().findFragmentById(R.id.index_goods);
        gf.setUrl(Constants.URL_INDEX_FLOOR);
        final Map map = new HashMap();
        map.put("type", 0);
        map.put("currPage",currPage);
        map.put("num", 10);
        gf.setParam(map);
        ReScrollView sv = (ReScrollView)view.findViewById(R.id.index_scroll);
        sv.setScrollChangeEvent(new ReScrollView.ScrollChangeEvent() {
            @Override
            public void onScrollChanged() {
                map.put("currPage",++currPage);
                gf.builder();
            }
        });

        //事件处理
        ImageButton category = (ImageButton)view.findViewById(R.id.index_category_btn);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebActivity.class);
                intent.putExtra("url",Constants.URL_CATEGORY);
                startActivity(intent);
            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.URL_INDEX_LOOP, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                super.onSuccess(statusCode, headers, res);
                try{
                    if(statusCode == 200&&res.getBoolean("success")){
                        JSONArray list = res.getJSONArray("result");
                        if(list.length() > 0){
                            ViewPager vp = (ViewPager)getActivity().findViewById(R.id.index_loops);
                            List<ImageView> imgs = new ArrayList<ImageView>(list.length());
                            for(int i=0,j=list.length();i<j;i++) {
                                JSONObject jo = list.getJSONObject(i);
                                ImageView iv = new ImageView(getActivity());
                                imgs.add(iv);
                                Utils.ayncLoadInternetImageView(iv,Constants.URL_IMAGE+jo.getString("imgUrl"));
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GoodsFragment gf = (GoodsFragment)getFragmentManager().findFragmentById(R.id.index_goods);
        gf.builder();
    }

}
