package com.djay.locatesecurly.models;

import java.io.Serializable;


/**
 * Model class for Storing session implements {@link Serializable}
 *
 * @author Dhananjay Kumar
 */
public class Session implements Serializable {
    private double startTimeStamp;
    private double endTimeStamp;
    private String sessionId;

    public Session() {
    }

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }

    public double getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(double startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public double getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(double endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
