/*
' History Header:      Version         - Date        - Developer Name   - Work Description
' History       :        1.0           - May-2016    - Dhananjay Kumar  - service class to perform notification calls
 */

/*
 ##############################################################################################
 #####                                                                                    #####

 #####     FILE              : AutoSyncService.Java 	                   				  #####
 #####     CREATED BY        : Dhananjay Kumar                                            #####        
 #####     CREATION DATE     : May-2016                                                   #####
 #####                                                                                    #####

 #####     MODIFIED  BY      : Dhananjay Kumar                                            #####          
 #####     MODIFIED ON       :                                                   		  #####

 #####                                                                                    #####

 #####     CODE BRIEFING     : AutoSyncService Class.                    		   		  #####
 #####                         A service class which performs Sync calls  				  #####
 #####					       separate from UI thread	                      		      #####
 #####                                                                                    #####

 #####     COMPANY           : SpotOn Software Pvt. Ltd.                                  #####                
 #####                                                                                    #####

 ##############################################################################################
 */
package com.djay.locatesecurly.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;

import com.djay.locatesecurly.utils.Constants;
import com.djay.locatesecurly.utils.StorageUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.writer.WriterProcessor;

import static com.djay.locatesecurly.utils.Constants.MAX_HUMAN_VOICE_FREQUENCY_IN_HZ;
import static com.djay.locatesecurly.utils.Constants.MIN_HUMAN_VOICE_FREQUENCY_IN_HZ;

/**
 * BackgroundRecordService used for audio recording on certain durations in the background.
 *
 * @author Dhananjay Kumar
 */
public class BackgroundRecordService extends Service {

    private static final String TAG = BackgroundRecordService.class.getName();

    /**
     * The constant ACTION_CANCEL.
     */
    public static String ACTION_CANCEL = "action_cancel";
    /**
     * The constant ACTION_UPDATE.
     */
    public static String ACTION_UPDATE = "action_update";
    /**
     * The constant CHECK_INTERVAL.
     */
    public static int CHECK_INTERVAL = 10;
    private int numOfHumanFeqCaptured = 0;
    private CheckRecordedFreqTask mCheckRecordedFreqTask;
    private RecordStopTask mRecordStopTask;
    private AudioDispatcher dispatcher;

    /**
     * Schedules service to run next time
     */
    private void scheduleNextUpdate() {
        Log.i(TAG, "scheduleNextUpdate");
        if (dispatcher != null && !dispatcher.isStopped()) dispatcher.stop();
        numOfHumanFeqCaptured = 0;
        getAlarmManager().cancel(getPendingIntent());
        long currentTimeMillis = System.currentTimeMillis();
        long nextUpdateTimeMillis = currentTimeMillis + CHECK_INTERVAL
                * DateUtils.SECOND_IN_MILLIS;

        getAlarmManager().set(AlarmManager.RTC, nextUpdateTimeMillis,
                getPendingIntent());
        stopSelf();
    }

    /**
     * Provide pendingIntent to register or unregister with AlarmManager
     **/
    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, this.getClass());
        intent.setAction(ACTION_UPDATE);
        return PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Provide AlarmManager to schedule or cancel next service call
     **/
    private AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        clearPrevCache();
        if (intent != null && intent.getAction() != null
                && intent.getAction().equals(ACTION_CANCEL)) {
            Log.i(TAG, "ACTION_CANCEL");
            getAlarmManager().cancel(getPendingIntent());
            stopSelf();
        } else if (intent != null && intent.getAction() != null
                && intent.getAction().equals(ACTION_UPDATE)) {
            Log.i(TAG, "ACTION_UPDATE");
            getAlarmManager().cancel(getPendingIntent());
            detectHumanFrequencies();
        } else {
            Log.i(TAG, "ACTION_SCHEDULE");
            scheduleNextUpdate();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void clearPrevCache() {
        if (mCheckRecordedFreqTask != null)
            mCheckRecordedFreqTask.cancel();
        if (mRecordStopTask != null)
            mRecordStopTask.cancel();
        if (dispatcher != null && !dispatcher.isStopped()) dispatcher.stop();
    }

    private void detectHumanFrequencies() {
        numOfHumanFeqCaptured = 0;
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(TarsosDSPAudioFormat.Encoding.PCM_SIGNED, 22050, 2 *
                8, 1, 2, 22050, ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder()));
        final File file = new File(StorageUtil.getAppExternalDataDirectoryFile().getAbsolutePath() + "/" + System
                .currentTimeMillis() + ".3gpp");
        try {
            RandomAccessFile wfile = new RandomAccessFile(file, "rw");
            AudioProcessor p1 = new WriterProcessor(format, wfile);
            dispatcher.addAudioProcessor(p1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024,
                new PitchDetectionHandler() {
                    @Override
                    public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                            AudioEvent audioEvent) {
                        final float pitchInHz = pitchDetectionResult.getPitch();
                        if (pitchInHz > MIN_HUMAN_VOICE_FREQUENCY_IN_HZ && pitchInHz < MAX_HUMAN_VOICE_FREQUENCY_IN_HZ)
                            numOfHumanFeqCaptured = numOfHumanFeqCaptured + 1;
                        Log.i("numOfHumanFeqCaptured: ", "" + numOfHumanFeqCaptured + ", pitchINHx: " + pitchInHz);
                    }
                }));
        new Thread(dispatcher, "Audio Dispatcher").start();
        mCheckRecordedFreqTask = new CheckRecordedFreqTask(file);
        new Timer().schedule(mCheckRecordedFreqTask, Constants.HUMAN_AUDIO_FREQ_CHECK_DURATION);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private class CheckRecordedFreqTask extends TimerTask {
        private File file;

        private CheckRecordedFreqTask(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            Log.i(TAG, "CheckRecordedFreqTask: numOfHumanFeqCaptured: " + numOfHumanFeqCaptured);
            if (numOfHumanFeqCaptured < 5) {
                Log.i(TAG, "------ NoHumanFreqFoundAction");
                if (dispatcher != null) dispatcher.stop();
                if (file != null && file.exists()) file.delete();
                scheduleNextUpdate();
            } else {
                Log.i(TAG, "++++++ HumanFreqFoundAction");
                mRecordStopTask = new RecordStopTask();
                new Timer().schedule(mRecordStopTask, Constants.FULL_RECORD_DURATION);
            }
        }
    }

    private class RecordStopTask extends TimerTask {
        @Override
        public void run() {
            Log.i(TAG, "RecordStopTask");
            if (dispatcher != null) dispatcher.stop();
            scheduleNextUpdate();
        }
    }
}