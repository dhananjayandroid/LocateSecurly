package com.djay.locatesecurly.models;

/**
 * Model class for Storing session Latitude and Longitude
 *
 * @author Dhananjay Kumar
 */
public class SessionLatLng {
    private double lat;
    private double lng;
    private String sessionId;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
