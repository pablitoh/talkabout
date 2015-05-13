package com.concon.talkabout.talkabout.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by OE on 13/05/2015.
 */
public class LanguageHelper {
    public static void loadApplicationLanguage(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("embriagados", 0);
        String locale = sharedPref.getString("languageToLoad","");
        if(!locale.isEmpty())
        {
            String languageToLoad  = locale; // your language
            Locale actualLocale = new Locale(languageToLoad);
            Locale.setDefault(actualLocale);
            Configuration config = new Configuration();
            config.locale = actualLocale;
            context.getResources().updateConfiguration(config,
                    context.getResources().getDisplayMetrics());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("languageToLoad",languageToLoad );
            editor.commit();
        }

    }

    /***
     * Force new Language
     * @param context
     * @param lang
     */
    public static void setApplicationLanguage(Context context, String lang)
    {
        SharedPreferences sharedPref = context.getSharedPreferences("embriagados", 0);

            String languageToLoad  = lang; // your language
            Locale actualLocale = new Locale(languageToLoad);
            Locale.setDefault(actualLocale);
            Configuration config = new Configuration();
            config.locale = actualLocale;
            context.getResources().updateConfiguration(config,
                    context.getResources().getDisplayMetrics());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("languageToLoad",languageToLoad );
            editor.commit();


    }
}
