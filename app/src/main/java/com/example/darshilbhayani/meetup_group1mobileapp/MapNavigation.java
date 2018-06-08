package com.example.darshilbhayani.meetup_group1mobileapp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;

import android.os.Build;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class MapNavigation extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    private MapNavigation curr;

    Dialog myDialog;
    ImageView imgView;

    Button buttonFromLoc;

    ImageView exit;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private GoogleMap mMap;

    private static final int REQUEST_LOCATION = 1;
    static Polyline polylineFinal = null;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Button button;
    TextView textView;
    LocationManager locationManager;
    String lattitude,longitude;

    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    Double destiLat = 37.351275;
    Double destiLon = -121.939584;

    LatLng destination;
    LatLng source;

    HashMap<String, Integer> drawableImg = new HashMap<>();
    HashMap<String, HashMap<String, String>> planData = new HashMap<>();


    private FirebaseDatabase mFireBaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mDatabase;

    static private Map<Marker, Event> markersData = new HashMap<>();
    static private Map<Marker, Boolean> markersVisibility = new HashMap<>();
    static private Map<Marker, String> markerType = new HashMap<>();

    HashMap<String,Event> event = new HashMap<>();
    String uId;

    @Override
    public void onResume() {
        try {
            super.onResume();
            if (mGoogleApiClient != null &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{

            super.onCreate(savedInstanceState);
            setContentView(R.layout.map_navigation);

            //----Navigation Drawer---

            Intent i = new Intent(this, MapNavigation.class);
            curr = this;
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_bar_maps);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    int id = item.getItemId();
                    Intent i;

                    if (id == R.id.nav_Map) {
                        i = new Intent(curr, MapsActivity.class);
                        startActivity(i);

                    } else if (id == R.id.nav_settings) {
                        i=new Intent(curr,Settings_main.class);
                        startActivity(i);
                    }
                    else if (id == R.id.nav_Addplan) {
                        i = new Intent(curr, CreatePage1.class);
                        startActivity(i);
                    }
                    else if (id == R.id.nav_myPlan) {
                        i = new Intent(curr, MyPlanHistory.class);
                        startActivity(i);
                    }
                    else if (id == R.id.nav_planHistory) {
                        i = new Intent(curr, ListPlanDetails.class);
                        startActivity(i);
                    }

                    else if (id == R.id.nav_profile) {
                        i = new Intent(curr, ProfilePage.class);
                        startActivity(i);
                    }

                    return true;
                }

            });

            //---Navigation Drawer-----

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            //textView = (TextView)findViewById(R.id.text_location);
            //buttonFromLoc = (Button)findViewById(R.id.exitNav);
            //buttonFromLoc.setOnClickListener(this);

            exit = (ImageView) findViewById(R.id.exit);


            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(curr,MyPlanHistory.class);
                    startActivity(i);
                }
            });

            myDialog = new Dialog(this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean checkLocationPermission(){
        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return  false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            super.onPause();

            //stop location updates when Activity is no longer active
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            mMap = googleMap;

            drawableImg.put("a",R.drawable.a);
            drawableImg.put("b",R.drawable.b);
            drawableImg.put("c",R.drawable.c);
            drawableImg.put("d",R.drawable.d);
            drawableImg.put("e",R.drawable.e);
            drawableImg.put("f",R.drawable.f);
            drawableImg.put("g",R.drawable.g);
            drawableImg.put("h",R.drawable.h);
            drawableImg.put("i",R.drawable.i);
            drawableImg.put("j",R.drawable.j);
            drawableImg.put("k",R.drawable.k);
            drawableImg.put("l",R.drawable.l);
            drawableImg.put("m",R.drawable.m);
            drawableImg.put("n",R.drawable.n);
            drawableImg.put("o",R.drawable.o);
            drawableImg.put("p",R.drawable.p);
            drawableImg.put("q",R.drawable.q);
            drawableImg.put("r",R.drawable.r);
            drawableImg.put("s",R.drawable.s);
            drawableImg.put("t",R.drawable.t);
            drawableImg.put("u",R.drawable.u);
            drawableImg.put("v",R.drawable.v);
            drawableImg.put("w",R.drawable.w);
            drawableImg.put("x",R.drawable.x);
            drawableImg.put("y",R.drawable.y);
            drawableImg.put("z",R.drawable.z);


            Event eventData = (Event) getIntent().getSerializableExtra("EVENT");
            String id = getIntent().getStringExtra("ID");

            Log.i("...eventData..",eventData.getEvent_date());

            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            }
            else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }

            try{
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            //destinationLocation(mMap);

            Log.i("getLan_dest",eventData.getLan_dest());
            Log.i("getLat_dest",eventData.getLat_dest());

            if(eventData!=null && eventData.getLan_dest()!=null && eventData.getLat_dest()!=null)
                destination = new LatLng(Double.parseDouble(eventData.getLan_dest()),Double.parseDouble(eventData.getLat_dest()));

            destinationLocation(mMap,destination,eventData.getEvent_name().toString());

            drawRoute(source,destination);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fillData(HashMap<String, HashMap<String, String>> planData) {
        HashMap<String, String> tmp = new HashMap<>();

        setData(tmp,"37.594678","-122.389865","Testing Plan1");
        planData.put("1",tmp);

        tmp = new HashMap<>();
        setData(tmp,"37.542967","-122.304735","Enjoying Plan1");
        planData.put("2",tmp);

        tmp = new HashMap<>();
        setData(tmp,"37.534766","-121.424872","Sexifying Plan1");
        planData.put("3",tmp);

    }

    private void setData(HashMap<String, String> tmp, String lon, String lat, String PlanNm) {

        tmp.put("SOURCE_LON",lon);
        tmp.put("SOURCE_LAT",lat);
        tmp.put("PLAN_NM",PlanNm);
    }

    protected synchronized void buildGoogleApiClient() {
        try{
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void destinationLocation(GoogleMap mMap, LatLng destination, String PlanNm) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this);
        try {
                Marker hamburg = mMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .title("Destination Location").icon(BitmapDescriptorFactory.fromResource(drawableImg.get(String.valueOf(PlanNm.charAt(0)).toLowerCase()))));
                // Move the camera instantly to hamburg with a zoom of 15.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 5000, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void drawRoute(LatLng source, LatLng finalDestination){
        try{
            if(source!=null && finalDestination!=null) {
                LatLng origin = source;
                LatLng destination = finalDestination;
                DrawRouteMaps.getInstance(this)
                        .draw(origin, destination, mMap);
                //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Origin Location");
                //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Destination Location");

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(origin)
                        .include(destination).build();
                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void getLocation() {
        try{
            if (ActivityCompat.checkSelfPermission(MapNavigation.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (MapNavigation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MapNavigation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                double latti = 0;
                double longi = 0;

                if (location != null) {
                    latti = location.getLatitude();
                    longi = location.getLongitude();
                    lattitude = String.valueOf(latti);
                    longitude = String.valueOf(longi);

                    //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                    source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(source);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));

                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,10),5000, null);

                } else  if (location1 != null) {
                    latti = location1.getLatitude();
                    longi = location1.getLongitude();
                    lattitude = String.valueOf(latti);
                    longitude = String.valueOf(longi);

                    //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                    source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(source);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));

                    mCurrLocationMarker = mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,10),5000, null);

                } else  if (location2 != null) {
                    latti = location2.getLatitude();
                    longi = location2.getLongitude();
                    lattitude = String.valueOf(latti);
                    longitude = String.valueOf(longi);

                    //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                    source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(source);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));

                    mCurrLocationMarker = mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,10),5000, null);

                }else{
                    Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void buildAlertMessageNoGps() {
        try{
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Turn ON your GPS Connection")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try{
            mLastLocation = location;
            if (mCurrLocationMarker != null)
            {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10),5000, null);
            drawRoute(latLng,destination);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setSmallestDisplacement(0.1F); //added
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //changed
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {



        return false;
    }


}
