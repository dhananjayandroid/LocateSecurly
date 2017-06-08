package com.djay.locatesecurly.services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.djay.locatesecurly.utils.Constants;

/**
 * BroadcastReceiver class for start {@link BackgroundLocationService} on device reboot
 *
 * @author Dhananjay Kumar
 */
public class LocationLoggerServiceManager extends BroadcastReceiver {

    public static final String TAG = LocationLoggerServiceManager.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Make sure we are getting the right intent
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            boolean mUpdatesRequested = false;
            // Open the shared preferences
            SharedPreferences mPrefs = context.getSharedPreferences(
                    Constants.APP_PACKAGE_NAME, Context.MODE_PRIVATE);
            /*
             * Get any previous setting for location updates
	         * Gets "false" if an error occurs
	         */
            if (mPrefs.contains(Constants.RUNNING)) {
                mUpdatesRequested = mPrefs.getBoolean(Constants.RUNNING, false);
            }
            if (mUpdatesRequested) {
                ComponentName comp = new ComponentName(context.getPackageName(), BackgroundLocationService.class
                        .getName());
                ComponentName service = context.startService(new Intent().setComponent(comp));

                if (null == service) {
                    // something really wrong here
                    Log.d(TAG, "Could not start service " + comp.toString());
                }
            }

        } else {
            Log.d(TAG, "Received unexpected intent " + intent.toString());
        }
    }
}