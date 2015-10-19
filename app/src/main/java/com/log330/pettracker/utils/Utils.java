package com.log330.pettracker.utils;

import com.log330.pettracker.model.CircleZone;
import com.log330.pettracker.model.SquareZone;
import com.log330.pettracker.model.Tracker;
import com.log330.pettracker.model.Zone;

import java.util.ArrayList;

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

    public static ArrayList<Zone> generateDummyZones() {
        ArrayList<Zone> zones = new ArrayList<Zone>();

        Zone zone = new CircleZone(-47.3393322, 74.2935423, 0.00002);
        zones.add(zone);

        zone = new SquareZone(-47.3393322, -47.3410001, -47.3593322, -47.3210001);
        zones.add(zone);

        return zones;
    }

    public static ArrayList<Tracker> generateDummyTrackers() {
        ArrayList<Tracker> trackers = new ArrayList<Tracker>();

        Tracker tracker = new Tracker(AvatarGenerator.generate(150, 150), "Fluffy le chien");
        trackers.add(tracker);

        tracker = new Tracker(AvatarGenerator.generate(150, 150), "Miaow le chat");
        trackers.add(tracker);

        tracker = new Tracker(AvatarGenerator.generate(150, 150), "Pitpit l'oiseau");
        trackers.add(tracker);

        return trackers;
    }
}
