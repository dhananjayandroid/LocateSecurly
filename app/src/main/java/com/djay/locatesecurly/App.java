package com.djay.locatesecurly;

import android.app.Application;

import com.djay.locatesecurly.utils.CoreSharedHelper;

/**
 * Application class extends @{@link Application}
 *
 * @author Dhananjay Kumar
 */
public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        intiSharedHelper();
    }

    /**
     * Initializes {@link Application}
     */
    private void initApplication() {
        instance = this;
    }

    /**
     * Initializes {@link CoreSharedHelper}
     */
    public synchronized void intiSharedHelper() {
        new CoreSharedHelper(this);
    }

}