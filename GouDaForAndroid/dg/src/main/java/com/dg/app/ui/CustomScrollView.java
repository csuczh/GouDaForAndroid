package com.dg.app.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by lenovo on 2015/12/14.
 */
public class CustomScrollView extends SwipeRefreshLayout{
    private GestureDetector mGestureDetector;
    public CustomScrollView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
        mGestureDetector=new GestureDetector(context,new YScrollDetctor());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev)&&mGestureDetector.onTouchEvent(ev);
    }

    class  YScrollDetctor extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY)>Math.abs(distanceX);
        }
    }
}
