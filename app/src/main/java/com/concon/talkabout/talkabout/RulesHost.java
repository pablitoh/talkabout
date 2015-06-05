package com.concon.talkabout.talkabout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.concon.talkabout.talkabout.communication.ListManager;

import adapters.CustomListAdapter;
import adapters.RulesPageAdapter;
import adapters.TabPagerAdapter;

/**
 * Created by Pablitoh on 02/06/2015.
 */

public class RulesHost extends FragmentActivity implements ListManager {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_container);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new RulesPageAdapter(getSupportFragmentManager(),
                RulesHost.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void updateDB() {
        CustomRulesFragment fragment =  (CustomRulesFragment) getSupportFragmentManager().getFragments().get(0);
        fragment.updateDB();
    }
}
