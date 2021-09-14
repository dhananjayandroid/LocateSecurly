package com.djay.locatesecurly.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.djay.locatesecurly.db.tables.SessionDetailsTable;
import com.djay.locatesecurly.db.tables.SessionTable;

/**
 * This class provides Content Provider for application database
 *
 * @author Dhananjay Kumar
 */
public class DatabaseProvider extends ContentProvider {

    private static final String UNKNOWN_URI = "Unknown URI ";

    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        dbHelper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int token = ContentDescriptor.URI_MATCHER.match(uri);

        Cursor result = null;

        switch (token) {
            case SessionTable.PATH_TOKEN: {
                result = doQuery(db, uri, SessionTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
            case SessionDetailsTable.PATH_TOKEN: {
                result = doQuery(db, uri, SessionDetailsTable.TABLE_NAME, projection,
                        selection, selectionArgs, sortOrder);
                break;
            }
        }

        return result;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        Uri result = null;

        switch (token) {
            case SessionTable.PATH_TOKEN: {
                result = doInsert(db, SessionTable.TABLE_NAME,
                        SessionTable.CONTENT_URI, uri, values);
                break;
            }
            case SessionDetailsTable.PATH_TOKEN: {
                result = doInsert(db, SessionDetailsTable.TABLE_NAME,
                        SessionDetailsTable.CONTENT_URI, uri, values);
                break;
            }

        }

        if (result == null) {
            throw new IllegalArgumentException(UNKNOWN_URI + uri);
        }

        return result;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        String table = null;
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        switch (token) {
            case SessionTable.PATH_TOKEN: {
                table = SessionTable.TABLE_NAME;
                break;
            }
            case SessionDetailsTable.PATH_TOKEN: {
                table = SessionDetailsTable.TABLE_NAME;
                break;
            }

        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        for (ContentValues cv : values) {
            db.insert(table, null, cv);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return values.length;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        int result = 0;

        switch (token) {
            case SessionTable.PATH_TOKEN: {
                result = doDelete(db, uri, SessionTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }
            case SessionDetailsTable.PATH_TOKEN: {
                result = doDelete(db, uri, SessionDetailsTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            }

        }

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = ContentDescriptor.URI_MATCHER.match(uri);

        int result = 0;

        switch (token) {
            case SessionTable.PATH_TOKEN: {
                result = doUpdate(db, uri, SessionTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }
            case SessionDetailsTable.PATH_TOKEN: {
                result = doUpdate(db, uri, SessionDetailsTable.TABLE_NAME, selection,
                        selectionArgs, values);
                break;
            }

        }

        return result;
    }

    /**
     * Performs query to specified table using the projection, selection and
     * sortOrder
     *
     * @param db            SQLiteDatabase instance
     * @param uri           ContentUri for watch
     * @param tableName     Name of table on which query has to perform
     * @param projection    needed projection
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @param sortOrder     sort order if necessary
     * @return Cursor cursor from the table tableName matching all the criterion
     */
    private Cursor doQuery(SQLiteDatabase db, Uri uri, String tableName,
                           String[] projection, String selection, String[] selectionArgs,
                           String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(tableName);
        Cursor result = builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        if (getContext() != null)
            result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    /**
     * performs update to the specified table row or rows
     *
     * @param db            SQLiteDatabase instance
     * @param uri           uri of the content that was changed
     * @param tableName     Name of table on which query has to perform
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @param values        content values to update
     * @return success or failure
     */
    private int doUpdate(SQLiteDatabase db, Uri uri, String tableName,
                         String selection, String[] selectionArgs, ContentValues values) {
        int result = db.update(tableName, values, selection, selectionArgs);
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    /**
     * deletes the row/rows from the table
     *
     * @param db            SQLiteDatabase instance
     * @param uri           uri of the content that was changed
     * @param tableName     Name of table on which query has to perform
     * @param selection     needed selection cases
     * @param selectionArgs needed selection arguments
     * @return success or failure
     */
    private int doDelete(SQLiteDatabase db, Uri uri, String tableName,
                         String selection, String[] selectionArgs) {
        int result = db.delete(tableName, selection, selectionArgs);
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    /**
     * insert rows to the specified table
     *
     * @param db         SQLiteDatabase instance
     * @param tableName  Name of table on which query has to perform
     * @param contentUri ContentUri to build the path
     * @param uri        uri of the content that was changed
     * @param values     content values to update
     * @return success or failure
     */
    private Uri doInsert(SQLiteDatabase db, String tableName, Uri contentUri,
                         Uri uri, ContentValues values) {
        long id = db.insert(tableName, null, values);
        Uri result = contentUri.buildUpon().appendPath(String.valueOf(id))
                .build();
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
}