package com.app.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.commons.Utils;

import java.util.List;

/**
 * Created by on 2015/7/10.
 */
public class GoodsView extends LinearLayout{

    public GoodsView(Context context) {
        super(context);
    }

    public GoodsView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public void builder(List<Goods> gs){
        this.setOrientation(VERTICAL);
        if(gs!=null&&gs.size()>0){
            GoodsContent gc;
            for(Goods g:gs){
                gc = new GoodsContent(getContext());
                gc.builder(g);
                this.addView(gc);
            }
        }
    }

    private static class GoodsContent extends LinearLayout{

        public GoodsContent(Context context){
            super(context);
            this.setOrientation(HORIZONTAL);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 1, 0, 1);
            this.setLayoutParams(lp);
            this.setPadding(5, 15, 5, 15);
            this.setBackgroundColor(getResources().getColor(R.color.white));
        }

        public void builder(Goods g){
            ImageView iv = new ImageView(this.getContext());
            int wh = Math.round(getResources().getDimension(R.dimen.goods_img_wh));
            iv.setLayoutParams(new ViewGroup.LayoutParams(wh, wh));
            iv.setImageResource(R.drawable.loading);
            Utils.asyncLoadInternetImageView(iv, g.iurl);
            TextView tv1 = new TextView(getContext());
            tv1.setText(g.getGname());
            tv1.setTextColor(getResources().getColor(R.color.black));
            TextView tv2 = new TextView(getContext());
            tv2.setText(g.getRemark());
            tv2.setTextColor(getResources().getColor(R.color.orange));
            TextView tv3 = new TextView(getContext());
            tv3.setText(g.getMprice());
            tv3.setTextColor(getResources().getColor(R.color.darkgray));
            tv3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv3.setPadding(20, 0, 0, 0);
            TextView tv4 = new TextView(getContext());
            tv4.setText("￥" + g.getPrice());
            tv4.setTextColor(getResources().getColor(R.color.red));
            LinearLayout ll2 = new LinearLayout(getContext());
            ll2.setOrientation(ll2.HORIZONTAL);
            ll2.addView(tv4);
            ll2.addView(tv3);
            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(ll.VERTICAL);
            ll.setGravity(Gravity.TOP);
            ll.setPadding(20, 0, 0, 0);
            ll.addView(tv1);
            ll.addView(tv2);
            ll.addView(ll2);
            this.addView(iv);
            this.addView(ll);
        }
    }

    public static class Goods{
        private String gname;
        private String iurl;
        private String gdurl;
        private long gid;
        private String remark;
        private String price;
        private String mprice;

        public Goods(){
        }

        public Goods(long gid, String gname, String iurl, String gdurl, String mprice, String price, String remark) {
            this.gname = gname;
            this.iurl = iurl;
            this.gdurl = gdurl;
            this.gid = gid;
            this.remark = remark;
            this.price = price;
            this.mprice = mprice;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getIurl() {
            return iurl;
        }

        public void setIurl(String iurl) {
            this.iurl = iurl;
        }

        public String getGdurl() {
            return gdurl;
        }

        public void setGdurl(String gdurl) {
            this.gdurl = gdurl;
        }

        public long getGid() {
            return gid;
        }

        public void setGid(long gid) {
            this.gid = gid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMprice() {
            return mprice;
        }

        public void setMprice(String mprice) {
            this.mprice = mprice;
        }
    }

}
