package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.DefaultAdListener;
import com.amazon.device.ads.InterstitialAd;
import com.concon.talkabout.talkabout.ads.CustomInterstitial;
import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.utils.LanguageHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class CharadesMainMenu extends Activity {

    private CustomInterstitial ad;
    private InterstitialAd interstitialAd;
    private boolean amazonLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageHelper.loadApplicationLanguage(getBaseContext());
        setContentView(R.layout.charades_main_menu);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("Charades Main Menu");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());
        ad = new CustomInterstitial(this);

        AdRegistration.setAppKey("a5b437e27b884a1ba87e831f5ca71ac1");
        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setListener(new MyCustomAdListener());
        this.interstitialAd.loadAd();
    }

    @Override
    public void onBackPressed() {
        if(amazonLoaded)
        {
            this.interstitialAd.showAd();
        }
        else
        {
            ad.showIfAvailable();
        }
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
                b.putInt("time", 1);
                break;
            case R.id.charadesSecondTimeOption:
                b.putInt("time", 2);
                break;
            case R.id.charadesThirdTimeOption:
                b.putInt("time", 3);
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        intent.putExtras(b);
        startActivity(intent);

    }

    class MyCustomAdListener extends DefaultAdListener
    {
        @Override
        public void onAdLoaded(Ad ad, AdProperties adProperties)
        {
            amazonLoaded = true;
        }

        @Override
        public void onAdFailedToLoad(Ad ad, AdError error)
        {
            amazonLoaded = false;
        }

        @Override
        public void onAdDismissed(Ad ad)
        {
        }
    }
}
