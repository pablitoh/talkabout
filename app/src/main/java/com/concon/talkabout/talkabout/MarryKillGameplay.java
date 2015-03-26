package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.concon.talkabout.talkabout.ads.CustomInterstitial;
import com.concon.talkabout.talkabout.service.MarryKillParserService;
import com.concon.talkabout.talkabout.service.ParserService;
import com.concon.talkabout.talkabout.R;
import com.facebook.AppEventsLogger;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;


public class MarryKillGameplay extends Activity {

    List<String> list;
    ParserService parser;
    private UiLifecycleHelper uiHelper;
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
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        ad.showIfAvailable();
        super.onBackPressed();
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

    public void shareMKF(View v){


        String option1 = ((TextView) findViewById(R.id.option1)).getText().toString();
        String option2 = ((TextView) findViewById(R.id.option2)).getText().toString();
        String option3 = ((TextView) findViewById(R.id.option3)).getText().toString();

        String link="https://www.facebook.com/PartyGamesMobileApp";
        String name="Party Games";
        String caption="Party Games is an Android application with 4 classical Party Games:";
        String picture=getResources().getString(R.string.mkfPostImage);
        String description="Marry One, Kill One, F**K One:\n\n" + option1 + ",\n" + option2 + ",\n" + option3 + "\n";



        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG))
        {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                    .setLink(link).setName(name).setCaption(caption).setDescription(description).setPicture(picture)
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You need to have Facebook App installed for this", Toast.LENGTH_LONG).show();
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