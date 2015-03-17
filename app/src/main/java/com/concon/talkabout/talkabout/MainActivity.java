package com.concon.talkabout.talkabout;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private Integer difficulty;
    float x1,x2;
    float y1, y2;

    /**
     * To Be replaced by XML Parser
     */
    List<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle b = getIntent().getExtras();

        difficulty = b.getInt("key");

        /**
         * To be replaced
         */
        list.add("Dragon Ball Z");
        list.add("Pokemon");
        list.add("Digimon");
        list.add("El Chavo del 8");
        list.add("La vanguardia Peronista");

    }

    private String getRandomFromList( List<String> list)
    {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        Random rand = new Random();
        String random = list.get(rand.nextInt(list.size()));
        while (random.equalsIgnoreCase(phraseField.getText().toString())) {
            random = list.get(rand.nextInt(list.size()));
        }
        return random;
    }
    public void changeTopic(View v) {
        TextView phraseField = (TextView) findViewById(R.id.phrase);

        phraseField.setText(getRandomFromList(list));

    }

    public boolean onTouchEvent(MotionEvent touchevent)
    {
            switch (touchevent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    x1 = touchevent.getX();
                    y1 = touchevent.getY();
                    break;
                }
                case MotionEvent.ACTION_UP:
                {
                    x2 = touchevent.getX();
                    y2 = touchevent.getY();
                    /*
                    Distancia de swipe
                     */
                    if ((x1 - x2) > 250)
                    {
                        TextView phraseField = (TextView) findViewById(R.id.phrase);

                        phraseField.setText(getRandomFromList(list));
                    }

                }
            }
            return false;
        }
    }
