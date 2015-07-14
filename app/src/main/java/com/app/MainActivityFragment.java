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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ViewPager vp;
    private List<ImageView> imgs = new ArrayList<ImageView>();
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
        map.put("type",8);
        map.put("currPage",currPage);
        map.put("num", 4);
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GoodsFragment gf = (GoodsFragment)getFragmentManager().findFragmentById(R.id.index_goods);
        gf.builder();
        if(this.imgs.size() == 0){
            vp = (ViewPager)getActivity().findViewById(R.id.index_loops);
            ImageView iv1 = new ImageView(getActivity());
            iv1.setBackgroundResource(R.drawable.default_loop);
            ImageView iv2 = new ImageView(getActivity());
            iv2.setBackgroundResource(R.drawable.default_loop2);
            this.imgs.add(iv1);
            this.imgs.add(iv2);
            vp.setAdapter(new LoopImgsAdapter(this.imgs));
        }
    }

}
