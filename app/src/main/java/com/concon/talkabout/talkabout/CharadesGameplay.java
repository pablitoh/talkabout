package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.concon.talkabout.talkabout.service.SingleFeedParserService;
import com.concon.talkabout.talkabout.utils.RandomHelper;
import com.concon.talkabout.talkabout.utils.TimeHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CharadesGameplay extends Activity {

    private SingleFeedParserService singleFeedParserService = new SingleFeedParserService();
    private List<String> list = new ArrayList<>();
    private CountDown timerCount ;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private final int CharadesTime = 300 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mimic_gameplay);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        try {
            list = singleFeedParserService.parseXml(1, this.getResources().openRawResource(R.raw.mimic), "mimic");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onUserLeaveHint() {

        if(timerCount!=null){
            timerCount.cancel();
        }
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        if(timerCount!=null){
            timerCount.cancel();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mimic_gameplay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMimic(View v) throws IOException, XmlPullParserException {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        phraseField.setTextColor(getResources().getColor(R.color.black));

        phraseField.setText(RandomHelper.getNextRandomString(list,getApplicationContext()));

        if(timerCount!=null)
        {
            timerCount.cancel();
        }
        timerCount = new CountDown(CharadesTime * 1000, 1000);
        timerCount.start();
    }

    public class CountDown extends CountDownTimer {
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mediaPlayer.stop();
            TextView tv = (TextView) findViewById(R.id.phrase);
            tv.setText("TIME-OUT!!!\n Time to DRINK !!");
            tv.setTextColor(getResources().getColor(R.color.red));

            TextView timer = (TextView) findViewById(R.id.timer);
            timer.setText("");

            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.wrong);
            try {

                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        mp.release();
                    }
                });


            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();

        }

        @Override
        public void onTick(long millisUntilFinished) {
            TextView timer = (TextView) findViewById(R.id.timer);
            timer.setText(TimeHelper.convertMilisToTimeFormat(millisUntilFinished));

            if(millisUntilFinished < 10000)
            {
                mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.tick);
                try {

                    mediaPlayer.prepare();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp)
                        {
                            mp.release();
                        }
                    });

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        }
    }
}
