package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.concon.talkabout.talkabout.service.INeverParserService;
import com.concon.talkabout.talkabout.service.SingleFeedParserService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class SpinWheelGameplay extends Activity {


    private static Bitmap imageOriginal, imageScaled;
    private static Matrix matrix;

    private static String DEBUG_TAG = "Spinning wheel";

    private ImageView dialer;
    private int dialerHeight, dialerWidth;
    private GestureDetector detector;

    // needed for detecting the inversed rotations
    private boolean[] quadrantTouched;

    private boolean allowRotating;

    private MediaPlayer spinningSound = null;
    private boolean loopStarted = false;

    private boolean isPlaying = false;

    private List<String> chaosRules, iNever, randomFacts;

    private INeverParserService iNeverParserService;

    private SingleFeedParserService singleFeedParserService;

    private String text = "";
    private int icon;
    private String sectionTitle="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinwheel_gameplay);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // load the image only once
        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.wheel_of_fortune);
        }

        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }

        detector = new GestureDetector(this, new MyGestureDetector());

        // there is no 0th quadrant, to keep it simple the first value gets ignored
        quadrantTouched = new boolean[] { false, false, false, false, false };

        allowRotating = true;

        dialer = (ImageView) findViewById(R.id.imageView_ring);
        dialer.setOnTouchListener(new MyOnTouchListener());

        dialer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    dialerHeight = dialer.getHeight();
                    dialerWidth = dialer.getWidth();

                    // resize
                    Matrix resize = new Matrix();
                    resize.postScale((float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getWidth(), (float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);

                    // translate to the image view's center
                    float translateX =  dialerWidth /2 - imageScaled.getWidth() / 2;

                    float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
                    matrix.postTranslate(translateX, translateY);

                    dialer.setImageBitmap(imageScaled);
                    dialer.setImageMatrix(matrix);
                }
            }

        });

        iNeverParserService = new INeverParserService();
        try {
            iNever = iNeverParserService.parseXml(2, this.getResources().openRawResource(R.raw.inever), "inever");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        singleFeedParserService = new SingleFeedParserService();

        try {
            chaosRules = singleFeedParserService.parseXml(2, this.getResources().openRawResource(R.raw.chaosrule), "chaos");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            randomFacts = singleFeedParserService.parseXml(2, this.getResources().openRawResource(R.raw.randomfacts), "randomfacts");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_spinwheel_gameplay, menu);
        return true;
    }

    private class MyOnTouchListener implements View.OnTouchListener {

        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    // reset the touched quadrants
                    for (int i = 0; i < quadrantTouched.length; i++) {
                        quadrantTouched[i] = false;
                    }


                    allowRotating = false;

                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateDialer((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP:

                    allowRotating = true;

                    break;
            }

            // set the touched quadrant to true
            quadrantTouched[getQuadrant(event.getX() - (dialerWidth / 2), dialerHeight - event.getY() - (dialerHeight / 2))] = true;

            detector.onTouchEvent(event);

            return true;
        }
    }


    /**
     */
    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // get the quadrant of the start and the end of the fling
            int q1 = getQuadrant(e1.getX() - (dialerWidth / 2), dialerHeight - e1.getY() - (dialerHeight / 2));
            int q2 = getQuadrant(e2.getX() - (dialerWidth / 2), dialerHeight - e2.getY() - (dialerHeight / 2));

            // the inversed rotations
            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {

                dialer.post(new FlingRunnable(-1 * (velocityX + velocityY)));
            } else {
                // the normal rotation
                dialer.post(new FlingRunnable(velocityX + velocityY));
            }

            return true;
        }
    }

    /**
     * A {@link Runnable} for animating the the dialer's fling.
     */
    private class FlingRunnable implements Runnable {

        public static final float VELOCITY = 1.0700F;
        // SLOW STOP   public static final float VELOCITY = 1.0100F;

        private float velocity;
        private View spinButton = null;

        public FlingRunnable(float velocity, View spinButton) {
            this.velocity = velocity;
            this.spinButton = spinButton;
        }

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {

            if(!isPlaying){
                spinningSound =  MediaPlayer.create(getApplicationContext(), R.raw.wheel_tick);
                spinningSound.start();
                isPlaying = true;
            }

            float actualVelocity = Math.abs(velocity);
            Log.d(DEBUG_TAG, "Velocity is " + actualVelocity);

            // limit the velocity of the wheel
            if(actualVelocity > 3000) {
                velocity = 1000;
            }

            if(actualVelocity > 1000) {

                rotateDialer(velocity / 30);
                velocity /= VELOCITY;

                // post this instance again
                dialer.post(this);
            }

            else if (actualVelocity > 400 && actualVelocity <= 700) {
                rotateDialer(velocity / 20);
                velocity /= VELOCITY;

                // post this instance again
                dialer.post(this);
            }

            else if (actualVelocity > 250 && actualVelocity <= 400) {
                rotateDialer(velocity / 10);
                velocity /= VELOCITY;

                // post this instance again
                dialer.post(this);
            }

            else if (actualVelocity > 150 && actualVelocity <= 250) {
                rotateDialer(velocity / 8);
                velocity /= VELOCITY;

                // post this instance again
                dialer.post(this);
            }

            else if  (actualVelocity > 15 && actualVelocity <= 150) {
                rotateDialer(velocity / 5);
                velocity /= VELOCITY;

                // post this instance again
                dialer.post(this);
            }
            else if (actualVelocity > 10 && actualVelocity <= 15) {
                rotateDialer(velocity / 3);
                velocity /= VELOCITY;

                // post this instance again
                dialer.post(this);

            } else if (actualVelocity > 5) {
                rotateDialer(velocity / 2);
                velocity /= VELOCITY;
                // post this instance again
                dialer.post(this);

            } else if(actualVelocity > 3 && actualVelocity <= 5 ) {
                rotateDialer(velocity / 1.5F);
                velocity /= VELOCITY;
                // post this instance again
                dialer.post(this);
            }
              else if(actualVelocity > 1 && actualVelocity <= 3 ) {
                rotateDialer(velocity / 1.2F);
                velocity /= VELOCITY;
                // post this instance again
                dialer.post(this);
            } else {
                getRewardFromWheelAngle();
                allowRotating = false;
                dialer.setEnabled(true);
                isPlaying = false;
                spinningSound.stop();
                if(spinButton != null) {
                    spinButton.setEnabled(true);
                }
            }

        }
    }

    /**
     * @return The angle of the unit circle with the image view's center
     */
    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    /**
     * @return The selected quadrant.
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    /**
     * Rotate the dialer.
     *
     * @param degrees The degrees, the dialer should get rotated.
     */
    private void rotateDialer(float degrees) {
        matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);

        if (allowRotating) {
            dialer.setEnabled(false);
        }

        dialer.setImageMatrix(matrix);
    }

    public void spinWheel(View v)
    {
        /**
         * we enable the button inside the FlingRunnable in order to know
         * that the wheel finished to spin before you can spin it again.
        **/
        v.setEnabled(false);
        Random random = new Random();
        dialer.setEnabled(false);
        dialer.post(new FlingRunnable(1500, v));
    }

    private void getRewardFromWheelAngle() {
        /**         * Get the matrix angle URL: http://stackoverflow.com/a/28307921/3248003
         */
        float[] v = new float[9];

        matrix.getValues(v);

        // calculate the degree of rotation
        float rAngle = Math.round(Math.atan2(v[Matrix.MSKEW_X],
                v[Matrix.MSCALE_X]) * (180 / Math.PI));
        /**
         * Convert 0-180 and -180-0 degrees to 0-360 URL:
         * http://stackoverflow.com/a/25725005/3248003
         */
        rAngle = (rAngle + 360) % 360;

        Random random = new Random();

        if(rAngle <= 30) {
            text =  chaosRules.get(random.nextInt(chaosRules.size()));
            icon = R.drawable.icon_skull;
            sectionTitle = "Chaos Rule";
        }
        else if(rAngle > 30 && rAngle<= 60) {
            text = "Pick someone to tell the TRUTH!! (or next time he/she drinks x2)";
            icon = R.drawable.icon_truth;
            sectionTitle = "Truth";
        }
        else if(rAngle > 60 && rAngle <= 90) {
            text = randomFacts.get(random.nextInt(randomFacts.size()));
            icon = R.drawable.icon_question;
            sectionTitle = "Random Fact";
        }
        else if(rAngle > 90 && rAngle <= 120) {
            text = "You Drink 1 shot";
            icon = R.drawable.icon_drink;
            sectionTitle = "Drink";
        }
        else if(rAngle > 120 && rAngle <= 150) {
            text =  "Vendetta: the last one that make you drink, drinks";
            icon = R.drawable.icon_vendetta;
            sectionTitle = "Vendetta";
        }
        else if(rAngle > 150 && rAngle <= 180) {
           text = "Cleanse all chaos rules, skip turn";
            icon = R.drawable.icon_broom;
            sectionTitle = "Clean Rules";
        }
        else if(rAngle > 180 && rAngle <= 210) {
           text = "Sacrifice: For every drink you take, pick one victim to drink with you";
            icon = R.drawable.icon_blood;
            sectionTitle = "Sacrifice";
        }
        else if(rAngle > 210 && rAngle <= 240) {
           text = chaosRules.get(random.nextInt(chaosRules.size()));
            icon = R.drawable.icon_skull;
            sectionTitle = "Chaos Rule";
        }
        else if(rAngle > 240 && rAngle <= 270) {
           text = randomFacts.get(random.nextInt(randomFacts.size()));
            icon = R.drawable.icon_question;
            sectionTitle = "Random Fact";
        }
        else if(rAngle > 270 && rAngle <= 300) {
           text = iNever.get(random.nextInt(chaosRules.size()));
            icon = R.drawable.icon_never;
            sectionTitle = "Never have I ever";
        }
        else if(rAngle > 300 && rAngle <= 330) {
           text = "World Wide Drink: Everybody drinks";
            icon = R.drawable.icon_world;
            sectionTitle = "Global Drink";
        }
        else if(rAngle > 330 && rAngle <= 360) {
           text = "Target Drink: pick someone to drink";
            icon = R.drawable.icon_target;
            sectionTitle = "Target Drink";
        }

        TranslateAnimation animate = new TranslateAnimation(0,-findViewById(R.id.frameContainer).getWidth(),0,0);
        animate.setDuration(500);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.frameContainer).setVisibility(View.GONE);
                findViewById(R.id.frameText).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.textInfo)).setText(text);
                ((TextView)findViewById(R.id.logoView)).setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
                ((TextView)findViewById(R.id.logoView)).setText(sectionTitle);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.frameContainer).startAnimation(animate);

    }

    public void dismiss(View v)
    {
        TranslateAnimation animate = new TranslateAnimation(0,-findViewById(R.id.frameText).getWidth(),0,0);
        animate.setDuration(500);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.frameText).setVisibility(View.GONE);

                TranslateAnimation animate = new TranslateAnimation(-findViewById(R.id.frameContainer).getWidth(),0,0,0);
                animate.setDuration(500);
                findViewById(R.id.frameContainer).startAnimation(animate);

                findViewById(R.id.frameContainer).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.frameText).startAnimation(animate);
    }
}