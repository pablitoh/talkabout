package com.decote.partygames;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.decote.partygames.utils.TimeHelper;
import com.decote.partygames.R;
import com.decote.partygames.ads.CustomInterstitial;
import com.decote.partygames.service.SingleFeedParserService;
import com.decote.partygames.utils.RandomHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CharadesGameplay extends Activity {

    private SingleFeedParserService singleFeedParserService = new SingleFeedParserService();
    private List<String> list = new ArrayList<>();
    private CountDown timerCount ;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private CustomInterstitial ad;
    private final int CharadesTime = 300 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mimic_gameplay);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        ad = new CustomInterstitial(this);
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
        ad.showIfAvailable();
        if(timerCount!=null){
            timerCount.cancel();
        }
        super.onBackPressed();
    }

    public void getMimic(View v) throws IOException, XmlPullParserException {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        phraseField.setTextColor(getResources().getColor(R.color.black));

        phraseField.setText(RandomHelper.getNextRandomString(list, getApplicationContext()));

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
            tv.setText(getString(R.string.timeout));
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
