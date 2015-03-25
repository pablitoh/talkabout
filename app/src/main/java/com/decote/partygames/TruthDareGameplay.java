package com.decote.partygames;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.decote.partygames.ads.CustomInterstitial;
import com.decote.partygames.R;
import com.decote.partygames.service.SingleFeedParserService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class TruthDareGameplay extends Activity {
    private SingleFeedParserService singleFeedParserService = new SingleFeedParserService();

    private List<String> list;
    private Random rand = new Random();
    private CustomInterstitial ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth_dare_gameplay);
        try {

            list = singleFeedParserService.parseXml(1, this.getResources().openRawResource(R.raw.truthdare), "truthDare");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        ad = new CustomInterstitial(this);
        mAdView.loadAd(adRequest);

    }

    public void getFate(View v) {
        TextView phraseField = (TextView) findViewById(R.id.phrase);
        String random = list.get(rand.nextInt(list.size()));
        phraseField.setText(random);
    }

    @Override
    public void onBackPressed() {
        ad.showIfAvailable();
        super.onBackPressed();
    }
}
