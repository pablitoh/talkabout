package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.utils.LanguageHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by OE on 11/05/2015.
 */
public class About extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageHelper.loadApplicationLanguage(getBaseContext());
        setContentView(R.layout.activity_about);
        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("About");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    public void backtoMenu(View view) {
        this.finish();
    }
}
