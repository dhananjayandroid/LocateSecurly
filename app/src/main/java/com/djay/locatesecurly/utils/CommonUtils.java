package com.djay.locatesecurly.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class contains all the common methods needed in application
 *
 * @author Dhananjay Kumar
 */
public class CommonUtils {

    /**
     * Formats timestamp to date
     *
     * @param timeStamp long value
     * @return date text
     */
    public static String formattedDate(long timeStamp) {
        try {
            if (timeStamp == 0)
                throw new Exception("Invalid timeStamp");
            DateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault());
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

}
