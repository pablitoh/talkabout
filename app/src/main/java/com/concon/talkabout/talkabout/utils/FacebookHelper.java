package com.concon.talkabout.talkabout.utils;

import android.graphics.Bitmap;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Pablito on 31/03/2015.
 */
public class FacebookHelper {

    public static void shareCurrentScreen(ShareDialog shareDialog,Bitmap bitmap)
    {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap).build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareDialog.show(content);
    }
}
