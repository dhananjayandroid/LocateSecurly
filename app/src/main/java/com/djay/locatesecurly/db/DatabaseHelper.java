package com.djay.locatesecurly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.djay.locatesecurly.db.tables.SessionDetailsTable;
import com.djay.locatesecurly.db.tables.SessionTable;

import java.text.MessageFormat;

/**
 * SqliteOpenHeler class for application database
 *
 * @author Dhananjay Kumar
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private static final String KEY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS {0} ({1})";
    private static final String KEY_DROP_TABLE = "DROP TABLE IF EXISTS {0}";

    private static final int CURRENT_DB_VERSION = 1;
    private static final String DB_NAME = "securly.db";

    /**
     * Constructor using context for the class
     *
     * @param context Context
     */
    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSessionTable(db);
        createSessionDetailsTable(db);
    }

    /**
     * creates Session Table in device database
     *
     * @param db SqliteDatabase instance
     */
    private void createSessionTable(SQLiteDatabase db) {
        String sessionTableFields = SessionTable.Cols.ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SessionTable.Cols.START +
                " INTEGER, " + SessionTable.Cols.END +
                " INTEGER, " + SessionTable.Cols.SESSION_ID +
                " VARCHAR UNIQUE";
        createTable(db, SessionTable.TABLE_NAME, sessionTableFields);
    }

    /**
     * creates Session Details Table in device database
     *
     * @param db SqliteDatabase instance
     */
    private void createSessionDetailsTable(SQLiteDatabase db) {
        String sessionDetailsTableFields = SessionDetailsTable.Cols.ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SessionDetailsTable.Cols.SESSION_ID +
                " VARCHAR, " + SessionDetailsTable.Cols.LAT +
                " INTEGER, " + SessionDetailsTable.Cols.LNG +
                " INTEGER";
        createTable(db, SessionDetailsTable.TABLE_NAME, sessionDetailsTableFields);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db, SessionTable.TABLE_NAME);
        dropTable(db, SessionDetailsTable.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Drops Table from device database
     *
     * @param db   SqliteDatabase instance
     * @param name TableName
     */
    private void dropTable(SQLiteDatabase db, String name) {
        String query = MessageFormat
                .format(DatabaseHelper.KEY_DROP_TABLE, name);
        db.execSQL(query);
    }

    /**
     * Creates Table in device database
     *
     * @param db     SqliteDatabase instance
     * @param name   TableName
     * @param fields ColumnFields
     */
    private void createTable(SQLiteDatabase db, String name, String fields) {
        String query = MessageFormat.format(DatabaseHelper.KEY_CREATE_TABLE,
                name, fields);
        db.execSQL(query);
    }
}