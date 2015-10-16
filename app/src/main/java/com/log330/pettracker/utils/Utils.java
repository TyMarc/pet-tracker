package com.log330.pettracker.utils;

/**
 * Created by marc-antoinehinse on 2015-10-16.
 */
public class Utils {

    public static long getMillisTime(final String timegmt) {
        String[] splitted = timegmt.split(":");
        int hours = Integer.valueOf(splitted[0]);
        int minutes = Integer.valueOf(splitted[1]);
        int secs = Integer.valueOf(splitted[2]);

        return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (secs * 1000);
    }
}
