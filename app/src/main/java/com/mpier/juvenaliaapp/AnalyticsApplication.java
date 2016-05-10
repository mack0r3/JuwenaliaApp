package com.mpier.juvenaliaapp;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 * <p/>
 * Created by Konpon96 on 10-May-16.
 */
public class AnalyticsApplication extends Application {
    private static Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setDryRun(false);

            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker.enableAutoActivityTracking(true);
            mTracker.enableExceptionReporting(true);
        }
        return mTracker;
    }

    /**
     * Screen name to be reported to Google Analytics
     *
     * @param screenName Screen name to be reported. Use getClass().getSimpleName()
     */
    synchronized public static void sendScreenName(String screenName){
        if (mTracker != null){
            mTracker.setScreenName(screenName);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    /**
     * Event to be reported to Google Analytics.
     *
     * @param category Category name
     * @param action Action name
     */
    synchronized public static void sendEvent(String category, String action){
        if (mTracker != null) {
            mTracker.send(new HitBuilders.EventBuilder(category, action).build());
        }
    }

}