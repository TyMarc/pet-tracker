package com.log330.pettracker.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.log330.pettracker.listener.FetchListener;
import com.log330.pettracker.model.GPSPoint;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by marc-antoinehinse on 2015-10-16.
 */
public class Server {
    private static final String TAG = "Server";
    private static final String ADDRESS = "http://data.sparkfun.com/output/AJxWnmoo7Ztnqm13NYEp.json";

    public static void fetchGPS(final Context context, final FetchListener listener) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ADDRESS;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i(TAG, response);
                        try {
                            ArrayList<GPSPoint> points = new ArrayList<GPSPoint>();
                            JSONArray json = new JSONArray(response);
                            for(int i=0; i<json.length();i++) {
                                if(!json.getJSONObject(i).getString("date").equals("0000-00-00")) {
                                    double latitude = json.getJSONObject(i).getDouble("latitude");
                                    double longitude = json.getJSONObject(i).getDouble("longitude");
                                    String timestampStr = json.getJSONObject(i).getString("timestamp");
                                    Date date = Date.valueOf(timestampStr);
                                    GPSPoint point = new GPSPoint(latitude, longitude, date.getTime());
                                }
                            }
                            if(listener != null) {
                                listener.onFetchSuccessful(points);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(listener != null) {
                                listener.onFetchError();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
                if(listener != null) {
                    listener.onFetchError();
                }
            }
        });


        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
