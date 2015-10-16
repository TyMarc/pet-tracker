package com.log330.pettracker.model;

/**
 * Created by marc-antoinehinse on 2015-10-16.
 */
public class GPSPoint {
    private double latitude;
    private double longitude;
    private long timestamp;

    public GPSPoint(double latitude, double longitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
