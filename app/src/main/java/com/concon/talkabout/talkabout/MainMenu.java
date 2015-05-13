package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.concon.talkabout.talkabout.analitycs.GoogleAnalyticsApp;
import com.concon.talkabout.talkabout.elements.CustomHorizontalScroll;
import com.concon.talkabout.talkabout.utils.AppRater;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Locale;


public class MainMenu extends Activity {

    CustomHorizontalScroll horizontalScrollView;
    TextView prevButton;
    TextView nextButton;
    private final int SCROLL_AMOUNT = 200;
    private Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String locale = getSharedPreferences("embriagados",0).getString("languageToLoad","");
        if(!locale.isEmpty())
        {
            changeLanguage(locale);
        }
        setContentView(R.layout.activity_main_menu);

        Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("Main Menu");
        t.enableAdvertisingIdCollection(true);
        t.send(new HitBuilders.AppViewBuilder().build());
        AppRater.app_launched(this);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        prevButton = (TextView) findViewById(R.id.prevButton);
        nextButton = (TextView) findViewById(R.id.nextButton);
        horizontalScrollView = (CustomHorizontalScroll) findViewById(R.id.carrousel);

        prevButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        horizontalScrollView.scrollTo(horizontalScrollView.getScrollX()-SCROLL_AMOUNT,0);
                        break;
                }
                return true;
            }
        });
        nextButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        horizontalScrollView.scrollTo(horizontalScrollView.getScrollX()+SCROLL_AMOUNT,0);
                        break;
                }
                return true;
            }
        });

        horizontalScrollView.setOnScrollViewListener(new CustomHorizontalScroll.OnScrollViewListener() {
                                                         public void onScrollChanged(CustomHorizontalScroll v, int l, int t, int oldl, int oldt) {
                                                             int maxScrollX = horizontalScrollView.getChildAt(0).getMeasuredWidth() - horizontalScrollView.getMeasuredWidth();
                                                             boolean endRight,endLeft = false;
                                                             if (horizontalScrollView.getScrollX() > 0) {
                                                                 prevButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left_small, 0, 0, 0);
                                                             } else {
                                                                 prevButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrowleft_grayed, 0, 0, 0);
                                                             }

                                                             if (horizontalScrollView.getScrollX() < maxScrollX) {
                                                                 nextButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_right_small, 0, 0, 0);
                                                             } else {
                                                                 nextButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrowright_grayed, 0, 0, 0);
                                                             }
                                                         }
                                                     }
        );

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
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() > 1) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void startGame(View v) {
        switch (v.getId()) {
            case R.id.iNeverButton:
                startActivity(new Intent(MainMenu.this, IneverMainMenu.class));
                break;
            case R.id.truthDareButton:
                startActivity(new Intent(MainMenu.this, SpinWheelMainMenu.class));
                break;
            case R.id.mimicButton:
                startActivity(new Intent(MainMenu.this, CharadesMainMenu.class));
                break;
            case R.id.marryButton:
                startActivity(new Intent(MainMenu.this, MarryKillGameplay.class));
                break;
            default:;
        }

    }

    public void changeLanguage(View v)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.popup_change_language, null);
        dialogBuilder.setView(dialog);
        final AlertDialog alertDialog = dialogBuilder.create();
        Button dialogButtonES = (Button) dialog.findViewById(R.id.spanishButton);
        Button dialogButtonEN = (Button) dialog.findViewById(R.id.englishButton);

        dialogButtonES.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changeLanguage("es");
                alertDialog.dismiss();
                recreate();
            }
        });
        dialogButtonEN.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changeLanguage("en");
                alertDialog.dismiss();
                recreate();
            }
        });
        alertDialog.show();
    }

private void changeLanguage(String localeString)
{
    String languageToLoad  = localeString; // your language
    Locale locale = new Locale(languageToLoad);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext().getResources().updateConfiguration(config,
            getBaseContext().getResources().getDisplayMetrics());

    SharedPreferences prefs = getSharedPreferences("embriagados", 0);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("languageToLoad",languageToLoad );
    editor.commit();
}

    public void loadAbout(View view) {
        startActivity(new Intent(MainMenu.this, About.class));
    }
}
