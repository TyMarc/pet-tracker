package com.log330.pettracker.listener;

import com.log330.pettracker.model.GPSPoint;

import java.util.ArrayList;

/**
 * Created by marc-antoinehinse on 2015-10-16.
 */
public interface FetchListener {
    void onFetchSuccessful(ArrayList<GPSPoint> points);
    void onFetchError();
}
