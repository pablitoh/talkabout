package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TruthDareGameplay extends Activity {

    private List<String> list;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = Arrays.asList(getString(R.string.truth), getString(R.string.dare), getString(R.string.dodge), getString(R.string.drink));
        setContentView(R.layout.activity_truth_dare_gameplay);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void getFate(View v) {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        String random = list.get(rand.nextInt(list.size()));
        phraseField.setText(random);
    }
}
