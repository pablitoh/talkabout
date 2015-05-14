package com.concon.talkabout.talkabout.elements;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by OE on 14/05/2015.
 */
public class CustomAlert extends AlertDialog {
    protected CustomAlert(Context context) {
        super(context);
    }

    protected CustomAlert(Context context, int theme) {
        super(context, theme);
    }

    protected CustomAlert(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
