package com.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.app.adapter.LoopImgsAdapter;
import com.app.commons.Constants;

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
        final GoodsFragment gf = (GoodsFragment)getFragmentManager().findFragmentById(R.id.index_goods);
        gf.setUrl(Constants.URL_INDEX_FLOOR);
        Map map = new HashMap();
        map.put("type",8);
        map.put("currPage",currPage);
        map.put("num", 4);
        gf.setParam(map);
        ScrollView sv = (ScrollView)view.findViewById(R.id.index_scroll);
        sv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom == 0&&!gf.getLoadState()){
                    gf.getParam().put("currPage", ++currPage);
                    gf.builder();
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
