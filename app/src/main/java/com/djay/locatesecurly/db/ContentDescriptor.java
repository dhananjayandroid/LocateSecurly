package com.djay.locatesecurly.db;

import android.content.UriMatcher;
import android.net.Uri;

import com.djay.locatesecurly.db.tables.SessionDetailsTable;
import com.djay.locatesecurly.db.tables.SessionTable;

/**
 * This class contains description about
 * application database content providers
 * @author Dhananjay Kumar  
 *
 */
public class ContentDescriptor {

	private static final String AUTHORITY = "com.djay.locatesecurly";
	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	static final UriMatcher URI_MATCHER = buildUriMatcher();

	
	/** @return UriMatcher for database table Uris */	 
	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

		matcher.addURI(AUTHORITY, SessionTable.PATH, SessionTable.PATH_TOKEN);
		matcher.addURI(AUTHORITY, SessionDetailsTable.PATH, SessionDetailsTable.PATH_TOKEN);
		// TODO other tables can be added

		return matcher;
	}
}