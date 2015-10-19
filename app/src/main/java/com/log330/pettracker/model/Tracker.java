package com.log330.pettracker.model;

import android.graphics.Bitmap;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class Tracker {
    private Bitmap avatar;
    private String name;

    public Tracker(Bitmap avatar, String name) {
        this.avatar = avatar;
        this.name = name;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }
}
