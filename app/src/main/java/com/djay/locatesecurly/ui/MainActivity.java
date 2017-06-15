package com.djay.locatesecurly.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.djay.locatesecurly.R;
import com.djay.locatesecurly.models.Session;
import com.djay.locatesecurly.services.BackgroundLocationService;
import com.djay.locatesecurly.services.BackgroundRecordService;
import com.djay.locatesecurly.utils.CoreSharedHelper;
import com.djay.locatesecurly.utils.LocationUtils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


/**
 * Activity class for showing home screen i.e. dash board screen extends {@link BaseActivity}
 *
 * @author Dhananjay Kumar
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.sw_locate)
    Switch swLocate;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.sw_track)
    Switch swTrack;
    @BindView(R.id.tv_record_history)
    TextView tvRecordHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    /**
     * initializes UI
     */
    private void initUi() {
        ButterKnife.bind(this);
        if (checkSessionRunning()) {
            swLocate.setChecked(true);
        }
        if (checkAudioTrackRunning()) {
            swTrack.setChecked(true);
        }
    }

    /**
     * Check if session is already running
     *
     * @return true if session is running
     */
    private boolean checkSessionRunning() {
        return CoreSharedHelper.getInstance().getSessionId() != null;
    }

    /**
     * Check if audio tracking is already running
     *
     * @return true if audio tracking  is running
     */
    private boolean checkAudioTrackRunning() {
        return CoreSharedHelper.getInstance().getAudioTrackId() != null;
    }

    @OnCheckedChanged(R.id.sw_locate)
    void onCheckChange(boolean check) {
        if (check) {
            startNewSession();
        } else {
            stopExistingSession();
        }
    }

    @OnCheckedChanged(R.id.sw_track)
    void onAudioTrackCheckChange(boolean check) {
        if (check) {
            startAudioTracking();
        } else {
            stopAudioTracking();
        }
    }

    /**
     * Stops Audio Tracking
     */
    private void stopAudioTracking() {
        if (checkAudioTrackRunning()) {
            CoreSharedHelper.getInstance().clearAudioTrackId();
            Intent syncServiceIntent = new Intent(this, BackgroundRecordService.class);
            syncServiceIntent.setAction(BackgroundRecordService.ACTION_CANCEL);
            startService(syncServiceIntent);
        }
    }

    /**
     * Starts Audio Tracking
     */
    private void startAudioTracking() {
        if (!permissionHelper.isAllPermissionAvailable()) {
            swLocate.setChecked(false);
            permissionHelper.setActivity(this);
            permissionHelper.requestPermissionsIfDenied();
            return;
        }
        if (!checkAudioTrackRunning()) {
            CoreSharedHelper.getInstance().saveAudioTrackId("" + UUID.randomUUID());
            startService(new Intent(this, BackgroundRecordService.class));
        }
    }

    @OnClick(R.id.tv_history)
    void onHistoryClick() {
        SessionsListActivity.start(this);
    }

    @OnClick(R.id.tv_record_history)
    void onAudioHistoryClick() {
        AudioListActivity.start(this);
    }

    /**
     * Stops existing session
     */
    private void stopExistingSession() {
        if (checkSessionRunning()) {
            Session session = dataManager.getSessionBySessionId(CoreSharedHelper.getInstance().getSessionId());
            session.setEndTimeStamp(System.currentTimeMillis());
            dataManager.saveSession(session);
            CoreSharedHelper.getInstance().clearSessionId();
            stopService(new Intent(this, BackgroundLocationService.class));
        }
    }

    /**
     * Starts new session
     */
    private void startNewSession() {
        if (!permissionHelper.isAllPermissionAvailable()) {
            swLocate.setChecked(false);
            permissionHelper.setActivity(this);
            permissionHelper.requestPermissionsIfDenied();
            return;
        }
        if (!checkSessionRunning()) {
            if (LocationUtils.isLocationEnabled(this)) {
                String sessionId = "" + UUID.randomUUID();
                CoreSharedHelper.getInstance().saveSessionId(sessionId);
                Session session = new Session(sessionId);
                session.setStartTimeStamp(System.currentTimeMillis());
                dataManager.saveSession(session);
                startService(new Intent(this, BackgroundLocationService.class));
            } else {
                LocationUtils.showSettingsAlert(this);
                swLocate.setChecked(false);
            }
        }
    }


}
