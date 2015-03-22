package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.concon.talkabout.talkabout.service.MarryKillParserService;
import com.concon.talkabout.talkabout.service.ParserService;
import com.facebook.AppEventsLogger;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;


public class MarryKill extends Activity {

    List<String> list;
    ParserService parser;
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marry_kill);
        parser = new MarryKillParserService();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_marry_kill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setLink("https://www.facebook.com/PartyGamesMobileApp").setApplicationName("Party Games").setCaption("Party Games is an Android application with 4 classical Party Games:").setDescription("Marry One, Kill One, F**K One:\n" + option1 + ",\n" + option2 + ",\n" + option3 + ",\n").setPicture(getResources().getString(R.string.IneverPostImage))
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }
}
