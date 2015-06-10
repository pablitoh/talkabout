package com.concon.talkabout.talkabout;


import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.dataType.RewardCard;
import com.concon.talkabout.talkabout.utils.LanguageHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import adapters.RulesPageAdapter;
import adapters.TabPagerAdapter;

/**
 * Created by Pablitoh on 28/04/2015.
 */
public class SpinWheelContainerActivity extends FragmentActivity implements SpinWheelGameplayTAB.RewardListener {

    ViewPager viewPager;
    // Tab titles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageHelper.loadApplicationLanguage(getBaseContext());
        setContentView(R.layout.spin_wheel_tab_container);
        String[] tabs = { getApplication().getString(R.string.playTab), getApplication().getString(R.string.historyTab)};
        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("SpinWheel Gameplay");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),
                SpinWheelContainerActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
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
    public void onReward(RewardCard reward) {
     SpinWheelHistoryTAB secondtab = (SpinWheelHistoryTAB) getSupportFragmentManager().getFragments().get(1);
            secondtab.addToList(reward);
    }


    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem()==1)
        {
            viewPager.setCurrentItem(0);
            viewPager.getChildAt(0).findViewById(R.id.logo_icono).setEnabled(true);
        }
        else
        {
            super.onBackPressed();
        }
    }

}
