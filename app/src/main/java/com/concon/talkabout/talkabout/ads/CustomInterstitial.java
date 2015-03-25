package com.concon.talkabout.talkabout.ads;

import android.content.Context;

import com.concon.talkabout.talkabout.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Pablito on 24/03/2015.
 */
public class CustomInterstitial {

    InterstitialAd interstitialAd;

    public CustomInterstitial(Context context) {

        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getResources().getString(R.string.adsInterstitialID));
        requestNewInterstitial();
    }

    public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    public InterstitialAd getInterstitialAd()
    {
        return this.interstitialAd;
    }

    public void showIfAvailable()
    {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
    }


}
