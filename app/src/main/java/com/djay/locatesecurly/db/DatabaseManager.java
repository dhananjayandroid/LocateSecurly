package com.djay.locatesecurly.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.djay.locatesecurly.db.tables.SessionDetailsTable;
import com.djay.locatesecurly.db.tables.SessionTable;
import com.djay.locatesecurly.models.Session;
import com.djay.locatesecurly.models.SessionLatLng;

import java.util.ArrayList;


/**
 * This class acts as an interface between database and UI. It contains all the
 * methods to interact with device database.
 *
 * @author Dhananjay Kumar
 */
public class DatabaseManager {

    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    /**
     * Save the Session to SessionTable
     *
     * @param session Session to save
     */
    public void saveSession(Session session) {
        ContentValues values = getContentValuesSessionsTable(session);

        String condition = SessionTable.Cols.SESSION_ID + "='"
                + session.getSessionId() + "'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(SessionTable.CONTENT_URI,
                null, condition, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            resolver.update(SessionTable.CONTENT_URI, values,
                    condition, null);
        } else {
            resolver.insert(SessionTable.CONTENT_URI, values);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * Get ContentValues from the session to insert it into
     * SessionsTable
     *
     * @param session Session
     */
    private ContentValues getContentValuesSessionsTable(Session session) {
        ContentValues values = new ContentValues();
        try {
            values.put(SessionTable.Cols.SESSION_ID,
                    session.getSessionId());
            values.put(SessionTable.Cols.START, session.getStartTimeStamp());
            values.put(SessionTable.Cols.END,
                    session.getEndTimeStamp());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }


    /**
     * Save the sessionLatLng to SessionDetailsTable
     *
     * @param sessionLatLng sessionLatLng to save
     */
    public void saveSessionDetails(SessionLatLng sessionLatLng) {
        ContentValues values = getContentValuesSessionDetailsTable(sessionLatLng);
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(SessionDetailsTable.CONTENT_URI, values);
    }

    /**
     * Get ContentValues from the SessionLatLng to insert it into Session Details Table
     *
     * @param sessionLatLng sessionLatLng
     */
    private ContentValues getContentValuesSessionDetailsTable(SessionLatLng sessionLatLng) {
        ContentValues values = new ContentValues();
        try {
            values.put(SessionDetailsTable.Cols.SESSION_ID,
                    sessionLatLng.getSessionId());
            values.put(SessionDetailsTable.Cols.LAT, sessionLatLng.getLat());
            values.put(SessionDetailsTable.Cols.LNG,
                    sessionLatLng.getLng());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * Provide all session from SessionTable
     *
     * @return List of Session
     */
    public ArrayList<Session> getAllSessions() {
        Cursor cursor = context.getContentResolver().query(
                SessionTable.CONTENT_URI, null, null, null,
                SessionTable.Cols.START + " DESC");
        ArrayList<Session> sessionArrayList = null;
        if (cursor != null && cursor.moveToFirst()) {
            sessionArrayList = getSessionsFromCursor(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return sessionArrayList;
    }

    /**
     * Provide all session from SessionTable
     *
     * @return List of Session
     */
    public Session getSessionBySessionId(String sessionId) {
        String condition = SessionTable.Cols.SESSION_ID + "='"
                + sessionId + "'";
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(SessionTable.CONTENT_URI,
                null, condition, null, null);
        Session session = null;
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Session> sessions = getSessionsFromCursor(cursor);
            if (sessions != null && sessions.size() != 0)
                session = sessions.get(0);
        }
        if (cursor != null) {
            cursor.close();
        }
        return session;
    }

    /**
     * Gives list of Session from cursor
     *
     * @param cursor Cursor from SessionTable
     * @return List of Session
     */
    private ArrayList<Session> getSessionsFromCursor(Cursor cursor) {
        ArrayList<Session> sessions = new ArrayList<>();
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Session session = new Session();
                try {
                    session.setSessionId(cursor
                            .getString(cursor
                                    .getColumnIndex(SessionTable.Cols.SESSION_ID)));
                    session.setStartTimeStamp(cursor
                            .getDouble(cursor
                                    .getColumnIndex(SessionTable.Cols.START)));
                    session.setEndTimeStamp(cursor
                            .getDouble(cursor
                                    .getColumnIndex(SessionTable.Cols.END)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sessions.add(session);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return sessions;
    }


    /**
     * Provides list of SessionLatLng from SessionDetailsTable
     *
     * @return List of SessionLatLng
     */
    public ArrayList<SessionLatLng> getSessionDetails(String SessionId) {
        Cursor cursor = context.getContentResolver().query(
                SessionDetailsTable.CONTENT_URI, null, SessionDetailsTable.Cols.SESSION_ID + " = '" + SessionId + "'",
                null,
                SessionDetailsTable.Cols.SESSION_ID + " ASC");
        ArrayList<SessionLatLng> sessionLatLngs = null;
        if (cursor != null && cursor.moveToFirst()) {
            sessionLatLngs = getSessionDetailsFromCursor(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return sessionLatLngs;
    }

    /**
     * Gives list of SessionLatLng from cursor
     *
     * @param cursor Cursor from SessionDetailsTable
     * @return List of SessionLatLng
     */
    private ArrayList<SessionLatLng> getSessionDetailsFromCursor(
            Cursor cursor) {
        ArrayList<SessionLatLng> sessionLatLngs = new ArrayList<>();
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                SessionLatLng sessionLatLng = new SessionLatLng();
                try {
                    sessionLatLng.setSessionId(cursor
                            .getString(cursor
                                    .getColumnIndex(SessionDetailsTable.Cols.SESSION_ID)));
                    sessionLatLng.setLat(cursor
                            .getDouble(cursor
                                    .getColumnIndex(SessionDetailsTable.Cols.LAT)));
                    sessionLatLng.setLng(cursor
                            .getDouble(cursor
                                    .getColumnIndex(SessionDetailsTable.Cols.LNG)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sessionLatLngs.add(sessionLatLng);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return sessionLatLngs;
    }

    /**
     * Clear all the table contents
     */
    @SuppressWarnings("unused")
    public void clearAllCache() {
        deleteAllSessions();
        deleteAllSessionDetails();
    }

    /**
     * Clear contents from SessionTable
     */
    private void deleteAllSessions() {
        try {
            context.getContentResolver().delete(SessionTable.CONTENT_URI, null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear contents from SessionDetailsTable
     */
    private void deleteAllSessionDetails() {
        try {
            context.getContentResolver().delete(SessionDetailsTable.CONTENT_URI, null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}