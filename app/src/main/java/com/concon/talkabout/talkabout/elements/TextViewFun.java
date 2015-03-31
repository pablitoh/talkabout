package com.concon.talkabout.talkabout.elements;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Pablito on 20/03/2015.
 */
public class TextViewFun extends TextView {
    public TextViewFun(Context context) {
        super(context);
        setFont(context, this);
    }

    public TextViewFun(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context, this);
    }

    public TextViewFun(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context, this);
    }

    private void setFont(Context context, TextView text) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Action_Man.ttf");
        text.setTypeface(font);
    }
}
