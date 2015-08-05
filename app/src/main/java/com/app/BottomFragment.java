package com.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.activity.CartActivtiy;
import com.app.activity.CategoryActivity;
import com.app.activity.LoginActivity;
import com.app.activity.MyCenterActivity;
import com.app.commons.Constants;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by on 2015/6/26.
 */
public class BottomFragment extends Fragment {

    private boolean isShow = false;
    private LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom,container,false);
        this.container = (LinearLayout) view.findViewById(R.id.container);
        ImageButton index = (ImageButton) view.findViewById(R.id.index);
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(BottomFragment.this.getActivity(), MainActivity.class);
                BottomFragment.this.getActivity().startActivity(intent);
            }
        });
        ImageButton category = (ImageButton) view.findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(BottomFragment.this.getActivity(), CategoryActivity.class);
                BottomFragment.this.getActivity().startActivity(intent);
            }
        });
        ImageButton cart = (ImageButton) view.findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(BottomFragment.this.getActivity(), CartActivtiy.class);
                BottomFragment.this.getActivity().startActivity(intent);
            }
        });
        ImageButton center = (ImageButton) view.findViewById(R.id.mycenter);
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.asyncHttpRequestGet(Constants.URL_ISNOTLOGIN, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        if (statusCode == 200) {
                            com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                            Intent intent = new Intent();
                            if (jo.getBoolean("success")) {
                                intent.setClass(BottomFragment.this.getActivity(), MyCenterActivity.class);
                            } else {
                                intent.setClass(BottomFragment.this.getActivity(), LoginActivity.class);
                                intent.putExtra("clazz", MyCenterActivity.class.getName());
                            }
                            startActivity(intent);
                        } else {
                            Toast.makeText(BottomFragment.this.getActivity(), statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }

    public void show(){
        this.container.setVisibility(View.VISIBLE);
        this.isShow = true;
    }

    public void hide(){
        this.container.setVisibility(View.GONE);
        this.isShow = false;
    }

    public void toggle(){
        if(this.isShow){
            hide();
        }else{
            show();
        }
    }
}
