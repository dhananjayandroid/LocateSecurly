package com.djay.locatesecurly.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Utility class contains methods to handle location related tasks
 *
 * @author Dhananjay Kumar
 */
public class LocationUtils {

    /**
     * Checks if the location is enabled.
     *
     * @param context Context
     * @return true if the location is enabled
     */
    public static boolean isLocationEnabled(Context context) {
        try {
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);
            // get GPS status
            boolean checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // get network provider status
            boolean checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                ToastUtils.longToast("No Service Provider is available");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Shows {@link AlertDialog} to move to enable location services
     *
     * @param context Context
     */
    public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Location is not Enabled!");
        alertDialog.setMessage("Do you want to turn on Location services?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
