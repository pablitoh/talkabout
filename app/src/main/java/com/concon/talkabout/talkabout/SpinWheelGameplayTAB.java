package com.concon.talkabout.talkabout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.concon.talkabout.talkabout.dataType.OptionsMap;
import com.concon.talkabout.talkabout.dataType.RewardCard;
import com.concon.talkabout.talkabout.utils.DbManager;

import java.util.Random;

/**
 * Created by OE on 28/04/2015.
 */

public class SpinWheelGameplayTAB extends Fragment {

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

    Random random = new Random();
    private int AMOUNT_OF_OPTIONS = 12;
    private float DEGREES_PER_OPTION = 360 / AMOUNT_OF_OPTIONS;
    RewardListener mCallback;
    private OptionsMap OptionsMapObj;

    // Container Activity must implement this interface
    public interface RewardListener {
        public void onReward(RewardCard rewardCard);
    }

    public void enableSpinButton() {
        ImageView spinButton = (ImageView) getActivity().findViewById(R.id.logo_icono);
        spinButton.setEnabled(true);
        dialer.setEnabled(true);
    }
    public void disableSpinButton()
    {
        ImageView spinButton = (ImageView) getActivity().findViewById(R.id.logo_icono);
        spinButton.setEnabled(false);
        dialer.setEnabled(false);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (RewardListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnReward");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.activity_spinwheel_gameplay, container, false);

        OptionsMapObj = new OptionsMap(getActivity());
        DbManager db = new DbManager(getActivity());
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
        ImageView spinButton = (ImageView) android.findViewById(R.id.logo_icono);

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                dialer.setEnabled(false);
                dialer.post(new FlingRunnable(random.nextInt((3000 - 1000) + 1) + 1000, v));
            }
        });
        if(spinButton != null) {
            spinButton.setEnabled(true);
        }
        detector = new GestureDetector(getActivity().getApplicationContext(), new MyGestureDetector());

        // there is no 0th quadrant, to keep it simple the first value gets ignored
        quadrantTouched = new boolean[] { false, false, false, false, false };

        allowRotating = true;

        dialer = (ImageView) android.findViewById(R.id.imageView_ring);
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
                    resize.postScale((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth(), (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
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
        return android;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enableSpinButton();
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
                    //getView().findViewById(R.id.logo_icono).setEnabled(false);
                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateDialer((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP:
                    allowRotating = true;
                    enableSpinButton();
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
            disableSpinButton();
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
                spinningSound =  MediaPlayer.create(getActivity().getApplicationContext().getApplicationContext(), R.raw.wheel_tick);
                spinningSound.start();
                isPlaying = true;
            }

            float actualVelocity = Math.abs(velocity);
            Log.d(DEBUG_TAG, "Velocity is " + actualVelocity);

            // limit the velocity of the wheel
            if(actualVelocity > 3000) {

                velocity = random.nextInt((1500 - 1000) + 1) + 1000;
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

        int option = (int) Math.floor(rAngle / DEGREES_PER_OPTION);
        RewardCard reward = OptionsMapObj.getMap().get(option).getReward();
        mCallback.onReward(reward);

        // custom dialog

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.reward_dialog, null);
        dialogBuilder.setView(dialog);
        final AlertDialog alertDialog = dialogBuilder.create();
        TextView text2 = (TextView) dialog.findViewById(R.id.dialogText);
        text2.setText(reward.cardDescription);
        ((TextView)dialog.findViewById(R.id.dialogTitle)).setText(reward.cardTitle);
        ((TextView)dialog.findViewById(R.id.dialogTitle)).setCompoundDrawablesWithIntrinsicBounds(0, reward.cardIcon, 0, 0);
        Button dialogButton = (Button) dialog.findViewById(R.id.dismissDialogButton);


        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                enableSpinButton();
            }
        });
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }




}
