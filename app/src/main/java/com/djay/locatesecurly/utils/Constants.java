package com.djay.locatesecurly.utils;

/**
 * Class containing all the constants used in application
 *
 * @author Dhananjay Kumar
 */
public final class Constants {

    public static final String RUNNING = "runningInBackground"; // Recording data in background
    public static final String APP_PACKAGE_NAME = "com.djay.locatesecurly";
    public static final String EXTRA_SESSION = "extra_session";
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 30;
    // Update frequency in milliseconds
    public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 30;
    // A fast frequency ceiling in milliseconds
    public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    /**
     * Suppress default constructor if non-instantiable
     */
    private Constants() {
        throw new AssertionError();
    }
}