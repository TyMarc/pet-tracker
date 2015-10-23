package com.log330.pettracker.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class Tracker {
    private Bitmap avatar;
    private String name;
    private boolean isEnabled;
    private ArrayList<GPSPoint> trackers;
    private ArrayList<Marker> markers;
    private Marker point;
    private long timestamp;

    public Tracker(Bitmap avatar, String name, boolean isEnabled) {
        this.avatar = avatar;
        this.name = name;
        this.isEnabled = isEnabled;
    }

    public Tracker(Bitmap avatar, String name, boolean isEnabled, ArrayList<GPSPoint> trackers, long timestamp) {
        this.avatar = avatar;
        this.name = name;
        this.isEnabled = isEnabled;
        trackers.remove(0);
        this.trackers = trackers;
        this.timestamp = timestamp;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled(){
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public ArrayList<GPSPoint> getPoints() {
        return trackers;
    }

    public Marker getPoint() {
        return point;
    }

    public void setPoint(Marker point) {
        this.point = point;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<Marker> markers) {
        this.markers = markers;
    }
}
