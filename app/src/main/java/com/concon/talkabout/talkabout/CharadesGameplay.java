package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.service.SingleFeedParserService;
import com.concon.talkabout.talkabout.utils.RandomHelper;
import com.concon.talkabout.talkabout.utils.TimeHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CharadesGameplay extends Activity {

    private SingleFeedParserService singleFeedParserService = new SingleFeedParserService();
    private List<String> list = new ArrayList<>();
    private CountDown timerCount;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int charadesTime = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charades_gameplay);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("Charades");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());

        Bundle b = getIntent().getExtras();
        charadesTime = b.getInt("time") * 60;

        try {
            list = singleFeedParserService.parseXml(1, this.getResources().openRawResource(R.raw.mimic), "mimic");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
    protected void onUserLeaveHint() {

        if (timerCount != null) {
            timerCount.cancel();
        }
        super.onUserLeaveHint();
    }

    @Override
    public void onBackPressed() {
        if (timerCount != null) {
            timerCount.cancel();
        }
        super.onBackPressed();
    }

    public void getMimic(View v) throws IOException, XmlPullParserException {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        phraseField.setTextColor(getResources().getColor(R.color.black));
        phraseField.setText(RandomHelper.getNextRandomString(list, getApplicationContext()));

        if (((TextView) findViewById(R.id.nextStartCharades)).getText().equals(getResources().getString(R.string.Start))) {
            ((TextView) findViewById(R.id.nextStartCharades)).setText(getResources().getString(R.string.Next));
        }

        if (timerCount != null) {
            timerCount.cancel();
        }

        if (phraseField.getText().equals(getString(R.string.noMoreOptions))) {
            phraseField.setTextColor(getResources().getColor(R.color.red));
            ((TextView) findViewById(R.id.timer)).setText("");
            findViewById(R.id.nextStartCharades).setVisibility(View.GONE);
        } else {
            timerCount = new CountDown(charadesTime * 1000, 1000);
            timerCount.start();
        }


    }

    public class CountDown extends CountDownTimer {
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mediaPlayer.stop();
            TextView tv = (TextView) findViewById(R.id.phrase);
            tv.setText(getString(R.string.timeout));
            tv.setTextColor(getResources().getColor(R.color.red));

            TextView timer = (TextView) findViewById(R.id.timer);
            timer.setText("");

            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
            try {

                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
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

            if (millisUntilFinished < 10000) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tick);
                try {

                    mediaPlayer.prepare();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
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
