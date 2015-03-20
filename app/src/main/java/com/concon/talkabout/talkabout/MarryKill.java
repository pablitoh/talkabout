package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.concon.talkabout.talkabout.service.MarryKillParserService;
import com.concon.talkabout.talkabout.service.ParserService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;


public class MarryKill extends Activity {

    List<String> list;
    ParserService parser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marry_kill);
        parser = new MarryKillParserService();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
}
