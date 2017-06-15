package com.djay.locatesecurly.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.djay.locatesecurly.R;

import java.util.ArrayList;

/**
 * Helper class to acquire run-time permission
 *
 * @author Dhananjay Kumar
 */
public class PermissionHelper {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_MIC = Manifest.permission.RECORD_AUDIO;
    private static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    @SuppressLint("StaticFieldLeak")
    private static PermissionHelper permissionHelper;
    private Activity activity;
    private ArrayList<String> requiredPermissions;
    private ArrayList<String> unGrantedPermissions = new ArrayList<>();

    private PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    public static synchronized PermissionHelper getInstance(Activity activity) {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper(activity);
        }
        return permissionHelper;
    }

    private void initPermissions() {
        requiredPermissions = new ArrayList<>();
        requiredPermissions.add(PERMISSION_ACCESS_FINE_LOCATION);
        requiredPermissions.add(PERMISSION_ACCESS_COARSE_LOCATION);
        requiredPermissions.add(PERMISSION_WRITE_EXTERNAL_STORAGE);
        requiredPermissions.add(PERMISSION_MIC);
    }

    public void requestPermissionsIfDenied() {
        unGrantedPermissions = getUnGrantedPermissionsList();
        if (canShowPermissionRationaleDialog()) {
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermissions();
                        }
                    });
            return;
        }
        askPermissions();
    }

    @SuppressWarnings("unused")
    public void requestPermissionsIfDenied(final String permission) {
        if (canShowPermissionRationaleDialog(permission)) {
            showMessageOKCancel(activity.getResources().getString(R.string.permission_message),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermission(permission);
                        }
                    });
            return;
        }
        askPermission(permission);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private boolean canShowPermissionRationaleDialog() {
        boolean shouldShowRationale = false;
        for (String permission : unGrantedPermissions) {
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if (shouldShow) {
                shouldShowRationale = true;
            }
        }
        return shouldShowRationale;
    }

    private boolean canShowPermissionRationaleDialog(String permission) {
        boolean shouldShowRationale = false;
        boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        if (shouldShow) {
            shouldShowRationale = true;
        }
        return shouldShowRationale;
    }

    private void askPermissions() {
        if (unGrantedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity, unGrantedPermissions.toArray(new String[unGrantedPermissions
                    .size()]), PERMISSION_REQUEST_CODE);
        }
    }

    private void askPermission(String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtils.longToast(R.string.permissions_denied);
                    }
                })
                .create()
                .show();
    }


    public boolean isAllPermissionAvailable() {
        boolean isAllPermissionAvailable = true;
        initPermissions();
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                isAllPermissionAvailable = false;
                break;
            }
        }
        return isAllPermissionAvailable;
    }

    private ArrayList<String> getUnGrantedPermissionsList() {
        ArrayList<String> list = new ArrayList<>();
        for (String permission : requiredPermissions) {
            int result = ActivityCompat.checkSelfPermission(activity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list;
    }

    @SuppressWarnings("unused")
    public boolean isPermissionAvailable(String permission) {
        boolean isPermissionAvailable = true;
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            isPermissionAvailable = false;
        }
        return isPermissionAvailable;
    }
}