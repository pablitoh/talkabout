package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.concon.talkabout.talkabout.ads.CustomInterstitial;
import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.service.MarryKillParserService;
import com.concon.talkabout.talkabout.service.ParserService;
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
import java.util.List;


public class MarryKillGameplay extends Activity {

    List<String> list;
    ParserService parser;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private CustomInterstitial ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marry_kill);
        parser = new MarryKillParserService();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        ad = new CustomInterstitial(getApplicationContext());
        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("MKF");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    @Override
    public void onBackPressed() {
        ad.showIfAvailable();
        super.onBackPressed();
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

    public void getPeople(View v) throws IOException, XmlPullParserException {
        int talkLevel;

        switch (v.getId()){
            case R.id.manButton:
                talkLevel =1 ;
                break;
            case R.id.womanButton:
                talkLevel =2 ;
                break;
            case R.id.allButton:
                talkLevel =3 ;
                break;
            default:talkLevel=3;
        }
        list = parser.parseXml(talkLevel,getResources().openRawResource(R.raw.marrykillfuck),"");

        TextView option = (TextView) findViewById(R.id.option1);
        option.setText(list.get(0));
        option = (TextView) findViewById(R.id.option2);
        option.setText(list.get(1));
        option = (TextView) findViewById(R.id.option3);
        option.setText(list.get(2));
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

    public void shareMKF(View v){
        String option1 = ((TextView) findViewById(R.id.option1)).getText().toString();
        String option2 = ((TextView) findViewById(R.id.option2)).getText().toString();
        String option3 = ((TextView) findViewById(R.id.option3)).getText().toString();

        String name="Party Games";

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(name)
                    .setContentDescription(getResources().getString(R.string.mkfPostText)+":"+option1+", "+option2+", "+option3)
                    .setContentUrl(Uri.parse("https://www.facebook.com/PartyGamesMobileApp"))
                    .setImageUrl((Uri.parse(getResources().getString(R.string.mkfPostImage))))
                    .build();

            shareDialog.show(linkContent);
        }

    }

    public void getInformation(View v)
    {
        TextView tv = (TextView) v;
        String name = tv.getText().toString();
        if(!name.equalsIgnoreCase(getString(R.string.firstTitle)) && !name.equalsIgnoreCase(getString(R.string.secondTitle)) && !name.equalsIgnoreCase(getString(R.string.thirdTitle)))
        {
            Uri uriUrl = Uri.parse("http://www.google.com/search?q="+name);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
    }


}
