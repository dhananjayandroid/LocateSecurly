package com.djay.locatesecurly.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Shared Helper class for storing values in {@link SharedPreferences}
 *
 * @author Dhananjay Kumar
 */
public class CoreSharedHelper {
    private static CoreSharedHelper instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedPreferencesEditor;

    public CoreSharedHelper(Context context) {
        instance = this;
        sharedPreferences = context.getSharedPreferences(Constants.NAME, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public static CoreSharedHelper getInstance() {
        if (instance == null) {
            throw new NullPointerException("CoreSharedHelper was not initialized!");
        }
        return instance;
    }

    private void delete(String key) {
        if (sharedPreferences.contains(key)) {
            sharedPreferencesEditor.remove(key).commit();
        }
    }

    private void savePref(String key, Object value) {
        delete(key);

        if (value instanceof Boolean) {
            sharedPreferencesEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            sharedPreferencesEditor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            sharedPreferencesEditor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            sharedPreferencesEditor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            sharedPreferencesEditor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            sharedPreferencesEditor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }

        sharedPreferencesEditor.commit();
    }

    private <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public String getSessionId() {
        return getPref(Constants.SESSION_ID, null);
    }

    public void saveSessionId(String id) {
        savePref(Constants.SESSION_ID, id);
    }

    public void clearSessionId() {
        savePref(Constants.SESSION_ID, null);
    }

    private class Constants {
        private static final String NAME = "LocateSecurly";

        private static final String SESSION_ID = "session_id";
    }

}