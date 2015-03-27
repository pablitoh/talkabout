package com.concon.talkabout.talkabout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class spinWhee extends ActionBarActivity {

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
    private MediaPlayer stoppingSound = null;
    private boolean loopStarted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel);

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
                    float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
                    float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
                    matrix.postTranslate(translateX, translateY);

                    dialer.setImageBitmap(imageScaled);
                    dialer.setImageMatrix(matrix);
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spin_wheel, menu);
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

    /**
     *
     */
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

        private float velocity;

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
            float actualVelocity = Math.abs(velocity);
            // limit the velocity of the wheel
            //nota: cambiar velocidad de sonido a
            Log.d(DEBUG_TAG, "Velocity is " + actualVelocity);

            if(actualVelocity > 3000) {
                velocity = 3000;
            }

            if(actualVelocity > 700) {
                if (!loopStarted) {
                    spinningSound = MediaPlayer.create(getApplicationContext(), R.raw.tick);
                    stoppingSound = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                    spinningSound.setLooping(Boolean.TRUE);
                    spinningSound.start();
                    loopStarted = true;
                }
                rotateDialer(velocity / 30);
                velocity /= 1.0700F;

                // post this instance again
                dialer.post(this);
            }

            else if (actualVelocity > 400 && actualVelocity <= 700) {
                rotateDialer(velocity / 20);
                velocity /= 1.0700F;

                // post this instance again
                dialer.post(this);
            }

            else if (actualVelocity > 250 && actualVelocity <= 400) {
                rotateDialer(velocity / 10);
                velocity /= 1.0100F;

                // post this instance again
                dialer.post(this);
            }

            else if (actualVelocity > 150 && actualVelocity <= 250) {
                rotateDialer(velocity / 8);
                velocity /= 1.0100F;

                // post this instance again
                dialer.post(this);
            }

            else if  (actualVelocity > 15 && actualVelocity <= 150) {

                if (!loopStarted) {
                    spinningSound = MediaPlayer.create(getApplicationContext(), R.raw.tick);
                    stoppingSound = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                    spinningSound.setLooping(Boolean.TRUE);
                    spinningSound.start();
                    loopStarted = true;
                }
                rotateDialer(velocity / 5);
                velocity /= 1.0100F;

                // post this instance again
                dialer.post(this);
            }
            else if (actualVelocity > 10 && actualVelocity <= 15) {
                rotateDialer(velocity / 3);
                velocity /= 1.0100F;

                // post this instance again
                dialer.post(this);

            } else if (actualVelocity > 5) {
                rotateDialer(velocity / 2);
                velocity /= 1.0100F;
                // post this instance again
                dialer.post(this);

            } else if(actualVelocity > 3 && actualVelocity <= 5 ) {
                rotateDialer(velocity / 1.5F);
                velocity /= 1.0100F;
                // post this instance again
                dialer.post(this);
            }
              else if(actualVelocity > 1 && actualVelocity <= 3 ) {
                if (loopStarted) {
                    spinningSound.stop();
                    loopStarted = false;
                }
                rotateDialer(velocity / 1.2F);
                velocity /= 1.0100F;
                // post this instance again
                dialer.post(this);

            } else {
                stoppingSound.start();
                getRewardFromWheelAngle();
                allowRotating = false;
                dialer.setEnabled(true);
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

    private void getReward(float angle) {
        int position = 0;

        // default reward
        int reward = 0;

        //sectors on our wheel, 360/sectors
        int numberOfSectors = 8;
        int circle = 360;
        float degreePerSector = (float) circle/numberOfSectors;

        //position of the reward in the wheel
        position = (int) Math.floor( angle / degreePerSector );
        switch (position) {
            case 0:
                Toast.makeText(spinWhee.this, "Pick someone to tell the TRUTH!! (or Drink x2)", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(spinWhee.this, "Pick someone to suffer the CONSEQUENCES!! (or Drink x2)", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(spinWhee.this, "Pick someone to: DRINK!", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(spinWhee.this, "The person who gave you the device: DRINKS!", Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(spinWhee.this, "You dodge this one!! Hand the device to someone else!!", Toast.LENGTH_LONG).show();
                break;
            case 5:
                Toast.makeText(spinWhee.this, "The person to your LEFT: DRINKS", Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(spinWhee.this, "The person to your RIGHT: DRINKS", Toast.LENGTH_LONG).show();
                break;
            case 7:
                Toast.makeText(spinWhee.this, "The person to your LEFT: Tells the TRUTH (or Drink x2)", Toast.LENGTH_LONG).show();
                break;
            case 8:
                Toast.makeText(spinWhee.this, "The person to your RIGHT: Tells the TRUTH (or Drink x2)", Toast.LENGTH_LONG).show();
                break;
            default:
                //never happens :)
                break;
        }

        Log.d(DEBUG_TAG, " Position is " + position + " Reward is " + reward + " " + degreePerSector);
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

        getReward(rAngle);
    }
}