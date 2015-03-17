package com.concon.talkabout.talkabout;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class IneverMainMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inever_main_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startActivity(View v) {
        Intent intent = new Intent(IneverMainMenu.this, IneverGameplay.class);
        Bundle b = new Bundle();

        switch (v.getId()) {
            case R.id.basicDifficulty:
                b.putInt("key", 1); //Your id
                break;
            case R.id.mediumDifficulty:
                b.putInt("key", 2); //Your id
                break;
            case R.id.hardDifficulty:
                b.putInt("key", 3); //Your id
                break;
            default:
                throw new RuntimeException("Unknow button ID");
        }

        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);

    }
}
