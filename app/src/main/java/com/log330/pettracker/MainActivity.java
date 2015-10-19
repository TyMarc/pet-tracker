package com.log330.pettracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.log330.pettracker.adapter.TrackerAdapter;
import com.log330.pettracker.adapter.ZoneAdapter;
import com.log330.pettracker.listener.FetchListener;
import com.log330.pettracker.model.GPSPoint;
import com.log330.pettracker.network.Server;
import com.log330.pettracker.utils.PreferencesController;
import com.log330.pettracker.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, FetchListener {
    private GoogleMap mMap;
    private Toolbar toolbar;
    private ZoneAdapter zoneAdapter;
    private TrackerAdapter trackerAdapter;

    public static void show(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUpMapIfNeeded();

        zoneAdapter = new ZoneAdapter(this, Utils.generateDummyZones());
        ((ListView) findViewById(R.id.list_zones)).setAdapter(zoneAdapter);

        trackerAdapter = new TrackerAdapter(this, Utils.generateDummyTrackers());
        ((ListView) findViewById(R.id.list_trackers)).setAdapter(trackerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Server.fetchGPS(this, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            PreferencesController.setPreference(this, PreferencesController.IS_ALREADY_LOGGED_IN, false);
            LoginActivity.show(this);
            finish();
        } else if (id == R.id.nav_tracker) {
            findViewById(R.id.maps_layout).setVisibility(View.GONE);
            findViewById(R.id.tracker_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.zone_layout).setVisibility(View.GONE);
            toolbar.setTitle(R.string.tracker);
        } else if (id == R.id.nav_zones) {
            findViewById(R.id.maps_layout).setVisibility(View.GONE);
            findViewById(R.id.tracker_layout).setVisibility(View.GONE);
            findViewById(R.id.zone_layout).setVisibility(View.VISIBLE);
            toolbar.setTitle(R.string.zones);
        } else if (id == R.id.nav_gps) {
            findViewById(R.id.maps_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.tracker_layout).setVisibility(View.GONE);
            findViewById(R.id.zone_layout).setVisibility(View.GONE);
            toolbar.setTitle(R.string.app_name);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFetchSuccessful(ArrayList<GPSPoint> points) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date;
        double totLong = 0;
        double totLat = 0;
        for(GPSPoint point : points) {
            date = new Date(point.getTimestamp());
            mMap.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude())).title("Tracker 1\n" + sdf.format(date)));
            totLong += point.getLongitude();
            totLat += point.getLatitude();
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(totLat / points.size(), totLong / points.size()), 17f));
    }

    @Override
    public void onFetchError() {

    }
}
