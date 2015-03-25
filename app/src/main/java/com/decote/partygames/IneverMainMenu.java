package com.decote.partygames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.decote.partygames.ads.CustomInterstitial;
import com.decote.partygames.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class IneverMainMenu extends Activity {

    CustomInterstitial ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inever_main_menu);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ad = new CustomInterstitial(this);
    }




    @Override
    public void onBackPressed() {
        ad.showIfAvailable();
        super.onBackPressed();
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
                throw new RuntimeException("Unknown button ID");
        }

        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);

    }
}
