package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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

    public void startGame(View v) {
        switch (v.getId()) {
            case R.id.iNeverButton:
                startActivity(new Intent(MainMenu.this, IneverMainMenu.class));
                break;
            case R.id.truthDareButton:
                startActivity(new Intent(MainMenu.this, TruthDareGameplay.class));break;
            case R.id.mimicButton:
                startActivity(new Intent(MainMenu.this, MimicGameplay.class));break;
            case R.id.marryButton:
                startActivity(new Intent(MainMenu.this, MarryKill.class));break;
            default:;
        }


    }
}
