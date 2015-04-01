package com.concon.talkabout.talkabout.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by pablito on 01/04/15.
 */
public class CustomHorizontalScroll extends HorizontalScrollView{

    private Runnable scrollerTask;
    private int initialPosition;

    private int newCheck = 100;
    private static final String TAG = "CustomHorizontalScroll";

    public interface OnScrollStoppedListener{
        void onScrollStopped();
    }
    public interface OnScrollViewListener {
        void onScrollChanged( CustomHorizontalScroll v, int l, int t, int oldl, int oldt );
    }

    private OnScrollViewListener mOnScrollViewListener;

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }

    private OnScrollStoppedListener onScrollStoppedListener;

    public CustomHorizontalScroll(Context context, AttributeSet attrs) {
        super(context, attrs);

        scrollerTask = new Runnable() {

            public void run() {

                int newPosition = getScrollX();
                if(initialPosition - newPosition == 0){//has stopped

                    if(onScrollStoppedListener!=null){

                        onScrollStoppedListener.onScrollStopped();
                    }
                }else{
                    initialPosition = getScrollX();
                    CustomHorizontalScroll.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void setOnScrollStoppedListener(CustomHorizontalScroll.OnScrollStoppedListener listener){
        onScrollStoppedListener = listener;
    }

    public void startScrollerTask(){
        initialPosition = getScrollX();
        CustomHorizontalScroll.this.postDelayed(scrollerTask, newCheck);
    }

}
