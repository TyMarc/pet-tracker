package com.log330.pettracker.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class Zone {
    private Polygon po;
    private boolean isEnabled;

    public Zone(Polygon po) {
        this.po = po;
        isEnabled = true;
    }

    public Polygon getPolygon() {
        return po;
    }

    public boolean isEnabled(){
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setPolygon(Polygon po) {
        this.po = po;
    }
}
