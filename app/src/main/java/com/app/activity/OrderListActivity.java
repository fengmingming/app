package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.Title;
import com.app.commons.Constants;
import com.app.commons.JSONArray;
import com.app.commons.ReScrollView;
import com.app.commons.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by on 2015/7/31.
 */
public class OrderListActivity extends Activity {

    private Button ispaid1;
    private Button ostatus4;
    private Button oall;
    private ReScrollView flush;
    private int page = 0;
    private int rows = 10;
    private int curBtn = 3;
    private LinearLayout container;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        Title title = (Title)getFragmentManager().findFragmentById(R.id.title);
        title.setTitle(getResources().getString(R.string.title_orders));
        ispaid1 = (Button) findViewById(R.id.ispaid1);
        ispaid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeTextColor();
                ispaid1.setTextColor(getResources().getColor(R.color.def_fontcolor));
                clearContainer();
                curBtn = 1;
                flushContainer();
            }
        });
        ostatus4 = (Button) findViewById(R.id.ostatus4);
        ostatus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeTextColor();
                ostatus4.setTextColor(getResources().getColor(R.color.def_fontcolor));
                clearContainer();
                curBtn = 2;
                flushContainer();
            }
        });
        oall = (Button) findViewById(R.id.oall);
        oall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeTextColor();
                oall.setTextColor(getResources().getColor(R.color.def_fontcolor));
                clearContainer();
                curBtn = 3;
                flushContainer();
            }
        });
        flush = (ReScrollView) findViewById(R.id.flush);
        flush.setScrollChangeEvent(new ReScrollView.ScrollChangeEvent() {
            @Override
            public void onScrollChanged() {
                flushContainer();
            }
        });
        container = (LinearLayout) findViewById(R.id.container);
        btnChangeTextColor();
        oall.setTextColor(getResources().getColor(R.color.def_fontcolor));
        curBtn = 3;
    }

    @Override
    protected void onStart() {
        super.onStart();
        clearContainer();
        flushContainer();
    }

    private void clearContainer(){
        page = 0;
        container.removeAllViews();
    }

    private void flushContainer(){
        if(isLoading){
            return;
        }
        isLoading = true;
        Map<String,Object> param = new HashMap<>();
        page ++ ;
        param.put("page",page);
        param.put("rows",rows);
        switch (curBtn){
            case 1:
                param.put("isPaid", 1);
                param.put("state", 1);
                ;break;
            case 2:
                param.put("status","3,4");
                param.put("state",1);
                ;break;
            case 3:;break;
        }
        Utils.asyncHttpRequestPost(Constants.URL_ORDERLIST, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                isLoading = false;
                if(statusCode == 200){
                    com.app.commons.JSONObject jo = new com.app.commons.JSONObject(res);
                    if(jo.getBoolean("success")){
                        jo = jo.getJSONObject("result");
                        if(jo != null){
                            JSONArray ja = jo.getJSONArray("entry");
                            if(ja != null&&ja.length() > 0){
                                for(int i=0,j=ja.length();i<j;i++){
                                    LinearLayout ll = createContainer(ja.getJSONObject(i));
                                    container.addView(ll);
                                }
                            }else{
                                Toast.makeText(OrderListActivity.this, getResources().getString(R.string.no_data_toast_text),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(OrderListActivity.this, jo.getString("errMsg"),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OrderListActivity.this, statusCode,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void btnChangeTextColor(){
        ispaid1.setTextColor(getResources().getColor(R.color.black));
        ostatus4.setTextColor(getResources().getColor(R.color.black));
        oall.setTextColor(getResources().getColor(R.color.black));
    }

    private LinearLayout createContainer(com.app.commons.JSONObject jo){
        LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hlp.setMargins(5,5,5,5);
        LinearLayout h = new LinearLayout(OrderListActivity.this);
        h.setOrientation(LinearLayout.VERTICAL);
        h.setLayoutParams(hlp);
        h.setBackgroundResource(R.drawable.border);
        h.setPadding(5,5,5,5);
        TextView orderNum = new TextView(OrderListActivity.this);
        orderNum.setText("订单号："+jo.getString("orderNum"));
        h.addView(orderNum);
        TextView ctime = new TextView(OrderListActivity.this);
        ctime.setText("下单时间："+jo.getString("createTime"));
        h.addView(ctime);
        TextView status = new TextView(OrderListActivity.this);
        if(jo.getInt("state") != 1){
            status.setText("订单状态："+Utils.createState(jo.getInt("state")));
        }else if(jo.getInt("isPaid") == 1){
            status.setText("订单状态：未付款");
        }else{
            status.setText("订单状态：" + Utils.createStatus(jo.getInt("status")));
        }
        h.addView(status);
        JSONArray ja = jo.getJSONArray("orderDetailList");
        if(ja != null&&ja.length() > 0){
            LinearLayout center = new LinearLayout(OrderListActivity.this);
            center.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams centerlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            centerlp.setMargins(1,1,1,1);
            center.setLayoutParams(centerlp);
            center.setBackgroundResource(R.drawable.border2);
            center.setPadding(5,5,5,5);
            int j = ja.length();
            if(j > 2){
                j = 2;
            }
            int wh = Math.round(getResources().getDimension(R.dimen.goods_img_wh)/2);
            for(int i=0;i<j;i++){
                com.app.commons.JSONObject goods = ja.getJSONObject(i);
                LinearLayout center_h = new LinearLayout(OrderListActivity.this);
                center_h.setPadding(5,5,5,5);
                ImageView iv = new ImageView(OrderListActivity.this);
                iv.setLayoutParams(new ViewGroup.LayoutParams(wh,wh));
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                Utils.asyncLoadInternetImageView(iv,Constants.URL_IMAGE+"/200X200"+goods.getString("photoUrl"));
                center_h.addView(iv);
                TextView goodsName = new TextView(OrderListActivity.this);
                goodsName.setPadding(10,5,5,5);
                goodsName.setText(goods.getString("goodsName")+"x"+goods.getString("number"));
                center_h.addView(goodsName);
                center.addView(center_h);
            }
            h.addView(center);
        }
        LinearLayout bottom = new LinearLayout(OrderListActivity.this);
        bottom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bottom.setGravity(Gravity.BOTTOM);
        TextView t1 = new TextView(OrderListActivity.this);
        t1.setText("订单金额：");
        TextView price = new TextView(OrderListActivity.this);
        LinearLayout.LayoutParams pricelp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pricelp.weight = 1;
        price.setLayoutParams(pricelp);
        price.setTextColor(getResources().getColor(R.color.def_fontcolor));
        price.setText("￥"+jo.getString("orderPrice"));
        bottom.addView(t1);
        bottom.addView(price);
        TextView btn = new TextView(OrderListActivity.this);
        int btnH = Math.round(getResources().getDimension(R.dimen.btn_height));
        LinearLayout.LayoutParams right = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, btnH);
        btn.setPadding(5,10,5,0);
        btn.setLayoutParams(right);
        btn.setText("订单详情 >");
        final long oid = jo.getLong("id");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra("id",oid);
                startActivity(intent);
            }
        });
        bottom.addView(btn);
        h.addView(bottom);
        return h;
    }
}
