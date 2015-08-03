package com.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Title extends Fragment {

    private String title;
    private BottomFragment navigation;
    private Intent backIntent;

    public Title() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackIntent(Intent backIntent){
        this.backIntent = backIntent;
    }

    public Intent getBackIntent(){
        return this.backIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title, container, false);
        this.navigation = (BottomFragment) getFragmentManager().findFragmentById(R.id.navigation);
        ImageView title_right = (ImageView) view.findViewById(R.id.title_right);
        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.toggle();
            }
        });
        final Activity activity = this.getActivity();
        ImageView back = (ImageView) view.findViewById(R.id.title_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(backIntent == null){
                   activity.onBackPressed();
               } else{
                   startActivity(backIntent);
               }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView tv = (TextView)getView().findViewById(R.id.list_title);
        tv.setText(this.title);
    }

}
