package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.concon.talkabout.talkabout.R;
import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("Main Menu");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());


    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if(event.getPointerCount() > 1) {
            return true;
        }
        else
            return super.onTouchEvent(event);
    }

    public void startGame(View v) {
        switch (v.getId()) {
            case R.id.iNeverButton:
                startActivity(new Intent(MainMenu.this, IneverMainMenu.class));
                break;
            case R.id.truthDareButton:
                startActivity(new Intent(MainMenu.this, TruthDareGameplay.class));
                break;
            case R.id.mimicButton:
                startActivity(new Intent(MainMenu.this, CharadesGameplay.class));
                break;
            case R.id.marryButton:
                startActivity(new Intent(MainMenu.this, MarryKillGameplay.class));
                break;
            default:
                ;
        }

    }


}
