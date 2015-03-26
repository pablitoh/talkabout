package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.service.INeverParserService;
import com.concon.talkabout.talkabout.service.ParserService;
import com.concon.talkabout.talkabout.utils.RandomHelper;
import com.facebook.AppEventsLogger;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class IneverGameplay extends Activity {

    private Integer difficulty;
    private UiLifecycleHelper uiHelper;

    /**
     * To Be replaced by XML Parser
     */
    List<String> list = new ArrayList<String>();

    private ParserService parserService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inever_gameplay);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("I Never");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());

        int difficulty = b.getInt("key");

        parserService = new INeverParserService();
        try {
            list =  parserService.parseXml(difficulty, this.getResources().openRawResource(R.raw.inever), "inever");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getRandomFromList(List<String> list)
    {
        return RandomHelper.getNextRandomString(list,getApplicationContext());
    }

    public void changeTopic(View v) {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        phraseField.setText(getRandomFromList(list));

        if(phraseField.getText().equals(getString(R.string.noMoreOptions)))
        {
            phraseField.setTextColor(getResources().getColor(R.color.red));
            ((TextView) findViewById(R.id.indicatorContainer)).setText("");
        }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    public void shareInever(View v){

        String option1 = ((TextView) findViewById(R.id.phrase)).getText().toString();
        if (FacebookDialog.canPresentShareDialog(getApplicationContext()))
        {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                    .setLink("https://www.facebook.com/PartyGamesMobileApp").setApplicationName("Party Games").setCaption("Party Games is an Android application with 4 classical Party Games:").setDescription(option1).setPicture(getResources().getString(R.string.IneverPostImage))
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "You need to have Facebook App installed for this", Toast.LENGTH_LONG).show();
        }
    }
}

