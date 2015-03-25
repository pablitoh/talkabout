package com.decote.partygames.utils;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;

/**
 * Created by pablito on 25/03/15.
 */
public class FacebookHelper {

    public static void publishFeedDialog(Context context, String link, String name, String caption, String description, String picture) {

        Bundle params = new Bundle();
        String errorString = "";
        params.putString("name", name);
        params.putString("description", description);
        params.putString("caption", caption);
        params.putString("link", link);
        params.putString("picture", picture);

        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(context,
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                            }
                        }
                    }

    })
                .build();
        feedDialog.show();
    }
}
