package com.concon.talkabout.talkabout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void changeTopic(View v) {
        TextView phraseField = (TextView) findViewById(R.id.phrase);

        /**
         * To Be replaced by XML Parser
         */
        List<String> list = new ArrayList<String>();
        list.add("Dragon Ball Z");
        list.add("Pokemon");
        list.add("Digimon");
        list.add("El Chavo del 8");
        list.add("La vanguardia Peronista");

        Random rand = new Random();
        String random = list.get(rand.nextInt(list.size()));
        while (random.equalsIgnoreCase(phraseField.getText().toString())) {
            random = list.get(rand.nextInt(list.size()));
        }

        phraseField.setText(random);

    }
}
