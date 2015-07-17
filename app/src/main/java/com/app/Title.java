package com.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class Title extends Fragment {

    private String title;

    public Title() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title, container, false);
        final Activity activity = this.getActivity();
        TextView back = (TextView) view.findViewById(R.id.title_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               activity.onBackPressed();
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
