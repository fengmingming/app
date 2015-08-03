package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.MainActivity;
import com.app.R;
import com.app.Title;
import com.app.commons.Utils;

/**
 * Created by on 2015/7/31.
 */
public class CommitOrderSucActivity extends Activity {
    private int payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_order_suc);
        Title title = (Title) getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.title_commit_order_suc));
        Intent intent = new Intent();
        intent.setClass(CommitOrderSucActivity.this, CartActivtiy.class);
        title.setBackIntent(intent);
        TextView orderNum = (TextView) findViewById(R.id.orderNum);
        TextView payPrice = (TextView) findViewById(R.id.payPrice);
        Button continue_pay = (Button) findViewById(R.id.continue_buy);
        Button pay = (Button) findViewById(R.id.pay);
        orderNum.setText(getIntent().getStringExtra("orderNum"));
        payPrice.setText(getIntent().getStringExtra("payPrice"));
        payType = getIntent().getIntExtra("payType",0);
        if(payType == 2){
            continue_pay.setVisibility(View.VISIBLE);
            continue_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CommitOrderSucActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            pay.setVisibility(View.VISIBLE);
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.pay(CommitOrderSucActivity.this, payType);
                }
            });
        }
    }
}
