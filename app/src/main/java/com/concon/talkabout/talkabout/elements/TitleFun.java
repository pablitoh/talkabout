package com.concon.talkabout.talkabout.elements;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Pablito on 19/03/2015.
 */
public class TitleFun extends TextView {

    public TitleFun(Context context) {
        super(context);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "PWStripes.ttf");
        this.setTypeface(font);
    }

    public TitleFun(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "PWStripes.ttf");
        this.setTypeface(font);
    }

    public TitleFun(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "PWStripes.ttf");
        this.setTypeface(font);
    }

}
