package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.concon.talkabout.talkabout.ads.CustomInterstitial;
import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class CharadesMainMenu extends Activity {

    CustomInterstitial ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charades_main_menu);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("Charades Main Menu");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());
        ad = new CustomInterstitial(this);
    }




    @Override
    public void onBackPressed() {
        ad.showIfAvailable();
        super.onBackPressed();
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

    public void startActivity(View v) {
        Intent intent = new Intent(CharadesMainMenu.this, CharadesGameplay.class);
        Bundle b = new Bundle();

        switch (v.getId()) {
            case R.id.charadesFirstTimeOption:
                b.putInt("time", 3);
                break;
            case R.id.charadesSecondTimeOption:
                b.putInt("time", 5);
                break;
            case R.id.charadesThirdTimeOption:
                b.putInt("time", 7);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        intent.putExtras(b);
        startActivity(intent);

    }
}
