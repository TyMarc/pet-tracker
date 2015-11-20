package com.log330.pettracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.log330.pettracker.adapter.TrackerAdapter;
import com.log330.pettracker.adapter.ZoneAdapter;
import com.log330.pettracker.listener.FetchListener;
import com.log330.pettracker.model.GPSPoint;
import com.log330.pettracker.model.Tracker;
import com.log330.pettracker.model.Zone;
import com.log330.pettracker.network.Server;
import com.log330.pettracker.utils.AvatarGenerator;
import com.log330.pettracker.utils.PreferencesController;
import com.log330.pettracker.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FetchListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private GoogleMap mMap;
    private Toolbar toolbar;
    private ZoneAdapter zoneAdapter;
    private TrackerAdapter trackerAdapter;
    private ArrayList<Marker> currentMarkers;

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

        currentMarkers = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUpMapIfNeeded();

        findViewById(R.id.fab).setOnClickListener(this);
        ArrayList<Zone> zones = new ArrayList<>();
        for (PolygonOptions z : Utils.generateDummyPolygonOptions()) {
            zones.add(new Zone(mMap.addPolygon(z)));
        }
        zoneAdapter = new ZoneAdapter(this, zones);
        ((ListView) findViewById(R.id.list_zones)).setAdapter(zoneAdapter);
        ((ListView) findViewById(R.id.list_zones)).setOnItemLongClickListener(this);
        ((ListView) findViewById(R.id.list_trackers)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tracker tracker = trackerAdapter.getItem(position);
                tracker.setEnabled(!tracker.isEnabled());
                if (tracker.getPoint() != null && tracker.getPoints() != null) {
                    if (tracker.isEnabled()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date date = new Date(tracker.getTimestamp());
                        tracker.setPoint(mMap.addMarker(new MarkerOptions().position(tracker.getPoint().getPosition()).title(tracker.getName()).snippet(sdf.format(date))));
                    } else {
                        if (tracker.getMarkers() != null && !tracker.getMarkers().isEmpty()) {
                            for (Marker m : tracker.getMarkers()) {
                                m.remove();
                            }
                            tracker.setMarkers(null);
                        }
                        tracker.getPoint().remove();
                    }
                }
                trackerAdapter.notifyDataSetChanged();
            }
        });

        ((ListView) findViewById(R.id.list_zones)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Zone zone = zoneAdapter.getItem(position);
                zone.setEnabled(!zone.isEnabled());
                zoneAdapter.notifyDataSetChanged();
                if (zone.isEnabled()) {
                    zone.setPolygon(mMap.addPolygon(new PolygonOptions().fillColor(Color.BLUE).strokeColor(Color.TRANSPARENT).addAll(zone.getPolygon().getPoints())));
                } else {
                    zone.getPolygon().remove();
                }
            }
        });
        trackerAdapter = new TrackerAdapter(this, new ArrayList<Tracker>());
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
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Server.fetchGPS(this, this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.logout);
            builder.setMessage(R.string.logout_desc);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferencesController.setPreference(MainActivity.this, PreferencesController.IS_ALREADY_LOGGED_IN, false);
                    LoginActivity.show(MainActivity.this);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();
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

        if (points.size() > 0) {
            Tracker tracker = new Tracker(AvatarGenerator.generate(150, 150), "Wiwi le ouistiti", true, points, points.get(0).getTimestamp());
            date = new Date(tracker.getTimestamp());
            tracker.setPoint(mMap.addMarker(new MarkerOptions().position(new LatLng(points.get(0).getLatitude(), points.get(0).getLongitude())).title(tracker.getName()).snippet(sdf.format(date))));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(points.get(0).getLatitude(), points.get(0).getLongitude()), 17f));
            trackerAdapter.add(tracker);
            trackerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFetchError() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        currentMarkers.add(mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                .fromResource(R.drawable.pin))));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (currentMarkers.contains(marker) && currentMarkers.size() >= 3) {
            PolygonOptions po = Utils.createSquare(currentMarkers);
            zoneAdapter.add(new Zone(mMap.addPolygon(po)));
            zoneAdapter.notifyDataSetChanged();
            for (Marker m : currentMarkers) {
                m.remove();
            }
            currentMarkers.clear();
        } else if (currentMarkers.contains(marker) && currentMarkers.size() < 3) {
            for (Marker m : currentMarkers) {
                m.remove();
            }
            currentMarkers.clear();
        } else {
            Tracker tracker = trackerAdapter.getTracker(marker);
            if (tracker != null) {
                if (tracker.getMarkers() != null && !tracker.getMarkers().isEmpty()) {
                    for (Marker m : tracker.getMarkers()) {
                        m.remove();
                    }
                    tracker.setMarkers(null);
                } else {
                    ArrayList<Marker> markers = new ArrayList<>();
                    for (GPSPoint p : tracker.getPoints()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date date = new Date(p.getTimestamp());
                        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(), p.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title(tracker.getName()).snippet(sdf.format(date))));
                    }
                    tracker.setMarkers(markers);
                }
                trackerAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_zone_title);
        builder.setMessage(R.string.delete_zone_desc);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Zone zone = zoneAdapter.getItem(position);
                zone.getPolygon().remove();
                zoneAdapter.remove(zone);
                zoneAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(R.string.add_tracker);

            LinearLayout ll = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setPadding(50, 0, 50, 0);

            final EditText input = new EditText(MainActivity.this);
            lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setHint(R.string.add_tracker_name);
            ll.addView(input);

            final EditText pin = new EditText(MainActivity.this);
            pin.setLayoutParams(lp);
            pin.setInputType(InputType.TYPE_CLASS_NUMBER);
            pin.setHint(R.string.add_tracker_pin);
            ll.addView(pin);
            alertDialog.setView(ll);

            alertDialog.setPositiveButton(R.string.link,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Tracker tracker = new Tracker(AvatarGenerator.generate(150, 150), input.getText().toString(), true);
                            trackerAdapter.add(tracker);
                            trackerAdapter.notifyDataSetChanged();
                        }
                    });

            alertDialog.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        }
    }
}
