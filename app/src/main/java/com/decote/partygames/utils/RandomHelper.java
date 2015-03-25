package com.decote.partygames.utils;

import android.content.Context;

import com.decote.partygames.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Pablito on 24/03/2015.
 */
public class RandomHelper {

    public static String getNextRandomString(List<String> list,Context context)
    {
        Random rand = new Random();
        int listSize = list.size();
        if(listSize > 0)
        {
            int randomNumber = rand.nextInt(listSize);
            String random = list.get(randomNumber);
            list.remove(randomNumber);
            return random;
        }
        else
        {
            return context.getResources().getString(R.string.noMoreOptions);
        }

    }
}
