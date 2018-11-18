package com.example.tudor.parkit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import java.util.Date;

import java.util.Timer;

import java.util.TimerTask;



class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater=null;

    PopupAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup=inflater.inflate(R.layout.popup, null);
        popup.setFocusable(true);
        popup.callOnClick();
        popup.setClickable(true);

        TextView tv=(TextView)popup.findViewById(R.id.title);

        tv.setText(marker.getTitle());
        tv=(TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getSnippet());
        Button park_here = popup.findViewById(R.id.parkhere);
        park_here.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("DEBUG:::","it works");
            }
        });

        return(popup);
    }
}

class MarkerInfo {
    Long id;
    String updatedAt;
    public MarkerInfo(Long id, String updatedAt) {
        this.id = id;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}


public class Maps2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 123;
    //private FusedLocationProviderClient mFusedLocationProviderClient;



    Map<Long, MarkerInfo> theMap = new HashMap<>();

    private GoogleMap mMap;
    private String username;
    private String password;


    class ClassExecutingTask {
        long delay = 1000 * 100; // delay in milliseconds
        LoopTask task = new LoopTask();
        Timer timer = new Timer("TaskName");



        public void start() {
            timer.cancel();
            timer = new Timer("TaskName");
            Date executionDate = new Date(); // no params = now
            timer.scheduleAtFixedRate(task, executionDate, delay);
        }

        private class LoopTask extends TimerTask {
            public void run() {

                getPinpoints();
            }

            private void getPinpoints() {
                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                String BASEURL ="https://murmuring-journey-83684.herokuapp.com";
                String PINPOINS ="/pinpoints";



                JsonArrayRequest objectRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        BASEURL+PINPOINS,
                        null,

                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    // Loop through the array elements
                                    //mMap.clear();
                                    for(int i=0;i<response.length();i++){
                                        // Get current json object
                                        JSONObject pinpoint = response.getJSONObject(i);
                                        LatLng pinpointLatLng = new LatLng(pinpoint.getDouble("latitude"), pinpoint.getDouble("longitude"));

                                        if (!theMap.containsKey(pinpoint.getLong("id"))) {
                                            mMap.addMarker(new MarkerOptions().position(pinpointLatLng).title(pinpoint.getString("title")));
                                            theMap.put(pinpoint.getLong("id"), new MarkerInfo(pinpoint.getLong("id"), pinpoint.getString("updatedAt")));
                                        }
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Eroare::::",error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params =  new HashMap<>();
                        String token = username+":"+password;
                        String finalToken = new String(Base64.encode(token.getBytes(),Base64.DEFAULT));
                        Log.d("finalToken", finalToken);
                        params.put("Authorization","Basic "+ finalToken);
                        //..add other headers
                        return params;
                    }
                };

                queue.add(objectRequest);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocation();
        // Add a marker in Sydney and move the camera

        PopupAdapter customInfoWindow = new PopupAdapter(getLayoutInflater());
        mMap.setInfoWindowAdapter(customInfoWindow);
        ClassExecutingTask executingTask = new ClassExecutingTask();
        executingTask.start();

    }





    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria mCriteria = new Criteria();
            String bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));


            Location mLocation = manager.getLastKnownLocation(bestProvider);
            if (mLocation != null) {
                Log.e("TAG", "GPS is on");
                final double currentLatitude = mLocation.getLatitude();
                final double currentLongitude = mLocation.getLongitude();
                LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }






        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);


        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        username = prefs.getString("first_name", null);
        password = prefs.getString("last_name", null);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        navUsername.setText(username);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps2, menu);
        return true;
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

        if (id == R.id.nav_mypark) {
            Intent screen2 = new Intent(Maps2Activity.this, MyParkActivity.class);
            startActivity(screen2);
        } else if (id == R.id.nav_report) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
