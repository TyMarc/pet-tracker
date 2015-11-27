package com.log330.pettracker.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolygonOptions;
import com.log330.pettracker.model.Tracker;
import com.log330.pettracker.model.Zone;

import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<PolygonOptions> generateDummyPolygonOptions() {
        ArrayList<PolygonOptions> zones = new ArrayList<PolygonOptions>();

        ArrayList<LatLng> points = new ArrayList<>();
        points.add(new LatLng(45.521955313232844, -73.5786148533225));
        points.add(new LatLng(45.52299758367935, -73.57757784426212));
        points.add(new LatLng(45.52170208346555, -73.57515648007393));
        points.add(new LatLng(45.52065720498781, -73.57615794986486));
        zones.add(createSquare(points));

        return zones;
    }

    public static PolygonOptions createSquare(final ArrayList<Marker> points) {
        PolygonOptions po = new PolygonOptions();

        for(Marker marker : points) {
            po.add(marker.getPosition());
        }

        po.fillColor(Color.BLUE);
        po.strokeColor(Color.TRANSPARENT);

        return po;
    }

    public static PolygonOptions createSquare(final List<LatLng> points) {
        PolygonOptions po = new PolygonOptions();

        for(LatLng ll : points) {
            po.add(ll);
        }

        po.fillColor(Color.BLUE);
        po.strokeColor(Color.TRANSPARENT);

        return po;
    }

    public static float getDistance(final LatLng firstPoint, final LatLng secondPoint) {
        double lat_a = firstPoint.latitude;
        double lng_a = firstPoint.longitude;
        double lat_b = secondPoint.latitude;
        double lng_b = secondPoint.longitude;

        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    public static int convertToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }
}
