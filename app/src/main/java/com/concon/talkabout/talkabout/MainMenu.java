package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
