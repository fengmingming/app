package com.app.commons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by on 2015/7/14.
 */
public class ReScrollView extends ScrollView {

    public interface ScrollChangeEvent{
        public void onScrollChanged();
    }

    private ScrollChangeEvent event;

    public ReScrollView(Context context){
        super(context);
    }

    public ReScrollView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public void setScrollChangeEvent(ScrollChangeEvent event) {
        this.event = event;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(getHeight() + getScrollY() >= computeVerticalScrollRange()&&this.event != null){
            event.onScrollChanged();
        }
    }
}
