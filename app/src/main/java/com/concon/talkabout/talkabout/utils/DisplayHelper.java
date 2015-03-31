package com.concon.talkabout.talkabout.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by Pablito on 31/03/2015.
 */
public class DisplayHelper {

    /***
     * Takes a screenshot of the current screen and return at an uncompressed bitmap
     * @param v
     * @return
     */
    public static Bitmap takeScreenShot(View v)
    {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
