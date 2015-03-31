package com.concon.talkabout.talkabout.analitycs;

import android.app.Application;

import com.concon.talkabout.talkabout.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;


/**
 * Created by Pablito on 26/03/2015.
 */
public class GoogleAnalyticsApp extends Application {

    private static final String PROPERTY_ID = "UA-61152808-2";

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
    }

    public HashMap mTrackers = new HashMap();

    public GoogleAnalyticsApp() {
        super();
    }

    public synchronized Tracker getTracker(TrackerName appTracker) {
        if (!mTrackers.containsKey(appTracker)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(appTracker, t);
        }
        return (Tracker) mTrackers.get(appTracker);
    }
}
