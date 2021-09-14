package com.djay.locatesecurly.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.djay.locatesecurly.db.DatabaseManager;
import com.djay.locatesecurly.ui.fragments.ProgressDialogFragment;
import com.djay.locatesecurly.utils.PermissionHelper;
import com.djay.locatesecurly.utils.bridges.LoadingBridge;


/**
 * Base Activity class for all Activities classes for application extends {@link AppCompatActivity}
 *
 * @author Dhananjay Kumar
 */
public class BaseActivity extends AppCompatActivity implements LoadingBridge {

    protected PermissionHelper permissionHelper;
    protected DatabaseManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = new DatabaseManager(this);
        permissionHelper = PermissionHelper.getInstance(this);
        if (!permissionHelper.isAllPermissionAvailable()) {
            permissionHelper.setActivity(this);
            permissionHelper.requestPermissionsIfDenied();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds back button to activity action bar
     */
    protected void setActivityUpEnabled() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public synchronized void showProgress() {
        ProgressDialogFragment.show(getSupportFragmentManager());
    }

    @Override
    public synchronized void hideProgress() {
        ProgressDialogFragment.hide(getSupportFragmentManager());
    }
}
