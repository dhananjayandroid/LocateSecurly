package com.djay.locatesecurly.db.tables;

import android.net.Uri;

import com.djay.locatesecurly.db.ContentDescriptor;


/**
 * This class describes all necessary info
 * about the Session Table of device database
 *
 * @author Dhananjay Kumar
 */
public class SessionTable {

    public static final String TABLE_NAME = "SessionTable";

    public static final String PATH = "session_table";

    public static final int PATH_TOKEN = 10;

    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of SessionTable
     *
     * @author Dhananjay Kumar
     */
    public static class Cols {
        public static final String ID = "_id";
        public static final String SESSION_ID = "session_id";
        public static final String START = "start";
        public static final String END = "end";
    }
}