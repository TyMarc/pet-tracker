package com.log330.pettracker.model;

import android.graphics.Bitmap;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class Tracker {
    private Bitmap avatar;
    private String name;
    private boolean isEnabled;

    public Tracker(Bitmap avatar, String name, boolean isEnabled) {
        this.avatar = avatar;
        this.name = name;
        this.isEnabled = isEnabled;
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
}
