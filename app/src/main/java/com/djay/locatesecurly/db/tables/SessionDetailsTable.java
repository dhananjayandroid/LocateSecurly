/*
' History Header:      Version         - Date        - Developer Name   - Work Description
' History       :        1.0           - Jan-2015    - Dhananjay Kumar  - class describes all necessary info about
the Activity Table
 */

/*
 ##############################################################################################
 #####                                                                                    #####

 #####     FILE              : SessionDetailsTable.Java 	       							      #####
 #####     CREATED BY        : Dhananjay Kumar                                            #####        
 #####     CREATION DATE     : Jan-2015                                                   #####

 #####                                                                                    #####

 #####     MODIFIED  BY      : Dhananjay Kumar                                            #####          
 #####     MODIFIED ON       :                                                   	      #####

 #####                                                                                    #####

 #####     CODE BRIEFING     : SessionDetailsTable Class.         		 			   		  #####
 #####                         class describes all necessary info about the Activity Table#####
 #####                                                                                    #####

 #####     COMPANY           : SpotOn Software Pvt. Ltd.                                  #####                
 #####                                                                                    #####

 ##############################################################################################
 */
package com.djay.locatesecurly.db.tables;

import android.net.Uri;

import com.djay.locatesecurly.db.ContentDescriptor;


/**
 * This class describes all necessary info
 * about the Session DetailsTable of device database
 *
 * @author Dhananjay Kumar
 */
public class SessionDetailsTable {

    public static final String TABLE_NAME = "SessionDetailsTable";

    public static final String PATH = "SESSION_DETAILS_TABLE";

    public static final int PATH_TOKEN = 20;

    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of SessionDetailsTable
     *
     * @author Dhananjay Kumar
     */
    public static class Cols {
        public static final String ID = "_id";
        public static final String SESSION_ID = "session_id";
        public static final String LAT = "lat";
        public static final String LNG = "lng";

    }
}