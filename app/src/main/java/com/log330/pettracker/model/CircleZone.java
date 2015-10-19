package com.log330.pettracker.model;

import android.graphics.Bitmap;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class CircleZone extends Zone {
    private double longitude;
    private double latitude;
    private double radius;

    public CircleZone(double latitude, double longitude, double radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }
}
