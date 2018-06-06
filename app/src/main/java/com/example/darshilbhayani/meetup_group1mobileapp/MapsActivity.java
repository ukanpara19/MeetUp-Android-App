package com.example.darshilbhayani.meetup_group1mobileapp;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

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


    private FirebaseDatabase mFireBaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mDatabase;

    HashMap<String,Event> event = new HashMap<>();
    String uId;

    private void darwPlanMarkers(GoogleMap mMap) {

        try{
            loadDatForJoinPlan();
            //fillData(planData);

            Log.i("Dattatat.ABCD.",event+"");
            Log.i("Dattatat..",event.entrySet()+"");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void loadDatForJoinPlan() {
        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFireBaseDatabase.getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        uId = user.getUid();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d("User!",user.getUid());
                }else{
                    Log.d("User","User is null!");
                }
            }
        };

        mDatabase.child("event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Total Record...!!",dataSnapshot.getChildrenCount()+"");
                for(DataSnapshot eventIDDataSet : dataSnapshot.getChildren())
                {
                   // Log.i(eventIDDataSet.getKey(),eventIDDataSet.getChildrenCount() + "");
                    Event e1 = new Event();
                    for(DataSnapshot eventDatalocal : eventIDDataSet.getChildren())
                    {
                        /*if(eventDatalocal.getKey().equals("ppl_joined"))
                             Log.i(eventDatalocal.getKey()+"**","--"+eventDatalocal.getValue().toString());*/

                        DataSnapshot eventData = eventDatalocal;

                        if(eventData.getKey().equals("email_id"))
                            e1.setEmail_id(eventData.getValue().toString());
                        if(eventData.getKey().equals("event_date"))
                            e1.setEvent_date(eventData.getValue().toString());
                        if(eventData.getKey().equals("event_dest"))
                            e1.setEvent_dest(eventData.getValue().toString());
                        if(eventData.getKey().equals("event_duration"))
                            e1.setEvent_duration(eventData.getValue().toString());
                        if(eventData.getKey().equals("event_name"))
                            e1.setEvent_name(eventData.getValue().toString());
                        if(eventData.getKey().equals("event_source"))
                            e1.setEvent_source(eventData.getValue().toString());
                        if(eventData.getKey().equals("event_time"))
                            e1.setEvent_time(eventData.getValue().toString());
                        if(eventData.getKey().equals("event_type"))
                            e1.setEvent_type(eventData.getValue().toString());
                        if(eventData.getKey().equals("lan_dest"))
                            e1.setLan_dest(eventData.getValue().toString());
                        if(eventData.getKey().equals("lan_source"))
                            e1.setLan_source(eventData.getValue().toString());
                        if(eventData.getKey().equals("lat_dest"))
                            e1.setLat_dest(eventData.getValue().toString());
                        if(eventData.getKey().equals("lat_source"))
                            e1.setLat_source(eventData.getValue().toString());
                        if(eventData.getKey().equals("ppl_joined"))
                            e1.setPpl_joined(eventData.getValue().toString());
                    }
                    event.put(eventIDDataSet.getKey(),e1);
                }

                Log.i("event.entrySet()..",event.keySet()+"");

                for (Map.Entry<String,Event> entryData : event.entrySet()){
                    Log.i("PlanId.........",entryData.getKey());
                    Event eventData = event.get(entryData.getKey());

                    Log.i("eventData.......",eventData.getPpl_joined()+"");

                    Double lat = Double.parseDouble(eventData.getLat_dest().toString());
                    Double lon = Double.parseDouble(eventData.getLan_dest().trim());
                    String PlanNm = eventData.getEvent_name();

                    Log.d("lat-long Heree!!!", "" + lat + "......." + lon);
                    LatLng sourceLocal = new LatLng((double)lon, (double)lat);

                /*

                MarkerOptions mo = new MarkerOptions().position(destination).
                        title("Jersey Shore, New Jersey").
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b)).
                        visible(true);

                Marker marker =  mMap.addMarker(mo);
                mo.anchor(0f, 0.5f);
                marker.showInfoWindow();
                 */

                    Marker marker =  mMap.addMarker(new MarkerOptions()
                            .position(sourceLocal)
                            .title(PlanNm).icon(BitmapDescriptorFactory.fromResource(drawableImg.get(String.valueOf(PlanNm.charAt(0)).toLowerCase()))));
                    marker.setTag(entryData.getKey());
                    markersData.put(marker,eventData);


                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Event dataModel = markersData.get(marker);
                            if(dataModel!=null) {
                                String title = dataModel.getEvent_name();

                                //Log.i("dataModel...", title);
                                Toast.makeText(MapsActivity.this,title,Toast.LENGTH_SHORT).show();

                                //ByDefault Making Join to the Event!!
                                //Start
                                String tmp = dataModel.getPpl_joined();
                                if(!tmp.trim().equals(""))
                                    tmp = tmp+";darshilbhayani92@gmail.com";
                                else
                                    tmp = "darshilbhayani92@gmail.com";
                                dataModel.setPpl_joined(tmp);

                                Log.i("dataModel..",dataModel.getPpl_joined());

                                Event eWriteData = new Event(dataModel.getEmail_id(),dataModel.getEvent_date(),dataModel.getEvent_dest(),
                                        dataModel.getEvent_duration(),dataModel.getEvent_name(),dataModel.getEvent_source(),dataModel.getEvent_time(),
                                        dataModel.getEvent_type(),dataModel.getLan_dest(),dataModel.getLan_source(),dataModel.getLat_dest(),
                                        dataModel.getLat_source(),dataModel.getPpl_joined());

                               //Log.i("ID",marker.getTag().toString());

                                mDatabase.child("event").child(marker.getTag().toString()).setValue(eWriteData);
                                Toast.makeText(MapsActivity.this,title+" Plan Successfully Joined!",Toast.LENGTH_SHORT).show();
                                //End
                            }
                            return false;
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
            setContentView(R.layout.activity_maps);

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
            //button = (Button)findViewById(R.id.button_location);

            button.setOnClickListener(this);
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

        //sourceLocation(mMap);
        destinationLocation(mMap);
        drawRoute(source,destination);
        darwPlanMarkers(mMap);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map<Marker, Event> markersData = new HashMap<>();

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

    private void destinationLocation(GoogleMap mMap) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this);
        try {
            // addresses = geocoder.getFromLocationName("Vilnius",5);
            addresses = geocoder.getFromLocationName("Jersey Shore, New Jersey",5);
            Log.i("addresses..","."+addresses.size());

            if(addresses.size() > 0) {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

                lat = destiLat;
                lon = destiLon;

                Log.d("lat-long", "" + lat + "......." + lon);
                destination = new LatLng(lat, lon);

                Marker hamburg = mMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .title("Jersey Shore, New Jersey").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b)));
                // Move the camera instantly to hamburg with a zoom of 15.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));

                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
            }

        } catch (IOException e) {
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
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(source);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));

                mCurrLocationMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                drawRoute(source,destination);

            } else  if (location1 != null) {
                 latti = location1.getLatitude();
                 longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(source);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));

                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                drawRoute(source,destination);

            } else  if (location2 != null) {
                 latti = location2.getLatitude();
                 longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(source);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));

                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                drawRoute(source,destination);

            }else{
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }

            if(Double.isNaN(latti) && Double.isNaN(longi)){
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(latti, longi, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();

                    Log.v("IGA", "Address" + add);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18),5000, null);
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
