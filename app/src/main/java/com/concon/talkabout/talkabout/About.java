package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by OE on 11/05/2015.
 */
public class About extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void backtoMenu(View view) {
        this.finish();
    }
}
