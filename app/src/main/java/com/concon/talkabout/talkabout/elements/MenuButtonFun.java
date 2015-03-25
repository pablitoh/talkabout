package com.concon.talkabout.talkabout.elements;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by Pablito on 19/03/2015.
 */
    public class MenuButtonFun extends Button {

        public MenuButtonFun(Context context) {
            super(context);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "PWStripes.ttf");
            this.setTypeface(font);

        }

        public MenuButtonFun(Context context, AttributeSet attrs) {
            super(context, attrs);

            Typeface font = Typeface.createFromAsset(context.getAssets(), "PWStripes.ttf");
            this.setTypeface(font);

        }

        public MenuButtonFun(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "PWStripes.ttf");
            this.setTypeface(font);
        }
    }

