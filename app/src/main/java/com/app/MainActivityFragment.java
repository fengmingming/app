package com.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.adapter.LoopImgsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ViewPager vp;
    private List<ImageView> imgs = new ArrayList<ImageView>();
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
