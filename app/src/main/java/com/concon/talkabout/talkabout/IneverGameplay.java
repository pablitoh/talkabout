package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.service.INeverParserService;
import com.concon.talkabout.talkabout.service.ParserService;
import com.concon.talkabout.talkabout.utils.RandomHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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

    /**
     * To Be replaced by XML Parser
     */
    List<String> list = new ArrayList<String>();
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private ParserService parserService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inever_gameplay);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle b = getIntent().getExtras();
        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("I Never");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        int difficulty = b.getInt("key");

        parserService = new INeverParserService();
        try {
            list = parserService.parseXml(difficulty, this.getResources().openRawResource(R.raw.inever), "inever");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getRandomFromList(List<String> list) {
        return RandomHelper.getNextRandomString(list, getApplicationContext());
    }

    public void changeTopic(View v) {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        phraseField.setText(getRandomFromList(list));

        if (phraseField.getText().equals(getString(R.string.noMoreOptions))) {
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
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void shareInever(View v) {

        String option1 = ((TextView) findViewById(R.id.phrase)).getText().toString();
        String title = getResources().getString(R.string.iNeverPostTitle);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(option1)
                    .setContentUrl(Uri.parse("https://www.facebook.com/PartyGamesMobileApp"))
                    .setImageUrl((Uri.parse(getResources().getString(R.string.IneverPostImage))))
                    .build();
            shareDialog.show(linkContent);
        }
    }
}

