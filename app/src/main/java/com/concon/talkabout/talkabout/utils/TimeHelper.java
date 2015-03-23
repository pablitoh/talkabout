package com.concon.talkabout.talkabout.utils;

/**
 * Created by Pablito on 23/03/2015.
 */
public class TimeHelper {
    public static String convertMilisToTimeFormat(long milis)
    {
        long second = (milis / 1000) % 60;
        long minute = (milis/ (1000 * 60)) % 60;
        return String.format("%02d:%02d", minute, second);
    }
}
