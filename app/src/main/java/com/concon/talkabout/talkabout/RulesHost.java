package com.concon.talkabout.talkabout;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.DefaultAdListener;
import com.amazon.device.ads.InterstitialAd;
import com.concon.talkabout.talkabout.adapters.ListPopulator;
import com.concon.talkabout.talkabout.ads.CustomInterstitial;
import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.communication.ListManager;
import com.concon.talkabout.talkabout.utils.LanguageHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import adapters.CustomListAdapter;
import adapters.RulesPageAdapter;
import adapters.TabPagerAdapter;

/**
 * Created by Pablitoh on 02/06/2015.
 */

public class RulesHost extends FragmentActivity implements ListManager {


    private CustomInterstitial ad;
    private InterstitialAd interstitialAd;
    private boolean amazonLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_container);
        LanguageHelper.loadApplicationLanguage(getBaseContext());

        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("My Rules");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());
        ad = new CustomInterstitial(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new RulesPageAdapter(getSupportFragmentManager(),
                RulesHost.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        AdRegistration.setAppKey("a5b437e27b884a1ba87e831f5ca71ac1");
        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setListener(new MyCustomAdListener());
        this.interstitialAd.loadAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
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


    @Override
    public void updateDB() {
        CustomRulesFragment fragment =  (CustomRulesFragment) getSupportFragmentManager().getFragments().get(0);
        fragment.updateDB();
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
    }

}
