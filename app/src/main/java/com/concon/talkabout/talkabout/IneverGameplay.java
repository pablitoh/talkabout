package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.concon.talkabout.talkabout.service.INeverParserService;
import com.concon.talkabout.talkabout.service.ParserService;
import com.facebook.AppEventsLogger;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setLink("https://www.facebook.com/PartyGamesMobileApp").setApplicationName("Party Games").setCaption("Party Games is an Android application with 4 classical Party Games:").setDescription(option1).setPicture(getResources().getString(R.string.IneverPostImage))
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }
}

