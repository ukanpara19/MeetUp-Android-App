package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.os.Build;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CreatePage2 extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    HashMap<String,String> event = new HashMap<>();
    Event event_data = new Event();
    EditText source_edittext,destination_edittext,event_name_edittext;
    TextView use_current_location_source_textview, use_current_location_dest_textview;
    ImageView source_current_location_imageview,dest_current_location_imageview,source_search_imageview,dest_search_imageview;
    Button add_date_time;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude,longitude;
    Marker mCurrLocationMarker;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
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
    }


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan2);

        event_name_edittext = findViewById(R.id.editText2);
        source_edittext = findViewById(R.id.editText3);
        destination_edittext = findViewById(R.id.editText4);

        use_current_location_source_textview = findViewById(R.id.sourceLoc);
        use_current_location_dest_textview = findViewById(R.id.destLoc);

        source_current_location_imageview = findViewById(R.id.imageView);
        source_search_imageview = findViewById(R.id.imageView5);

        dest_current_location_imageview = findViewById(R.id.imageView2);
        dest_search_imageview = findViewById(R.id.imageView6);

        add_date_time = findViewById(R.id.button4);

        Intent intent = getIntent();
        event = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        String event_type = intent.getStringExtra("event_object");


        //Search a location

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_mMap);
        mapFragment.getMapAsync(this);
    }

    private void destinationLocation(GoogleMap mMap,String search_location,String source_dest ) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this);
        try {
            // addresses = geocoder.getFromLocationName("Vilnius",5);
            addresses = geocoder.getFromLocationName(search_location,5);
            Log.i("addresses..","."+addresses.size());

            if(addresses.size() > 0)
            {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

                Log.d("lat-long", "" + lat + "......." + lon);

                LatLng destination = new LatLng(lat, lon);
                Log.d(source_dest,"source_search_imageview");

                if(source_dest == "source_search_imageview"){
                    Marker hamburg = mMap.addMarker(new MarkerOptions()
                            .position(destination)
                            .title(search_location).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
                    event.put("lat_source",lat.toString());
                    event.put("lan_source",lon.toString());
                }
                else{
                    Marker hamburg = mMap.addMarker(new MarkerOptions()
                            .position(destination)
                            .title(search_location).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
                    event.put("lat_dest",lon.toString());
                    event.put("lan_dest",lat.toString());
                }
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLocaation(String source_dest) {
        if(checkLocationPermission()){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try{
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation(source_dest);
                }
            }
            catch (Exception e){

            }
        }else{
            Toast.makeText(CreatePage2.this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
        }

    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Turn on Location!")
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
    }

    private void getLocation(String source_dest_get) {
        if (ActivityCompat.checkSelfPermission(CreatePage2.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (CreatePage2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CreatePage2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

                Log.d("lat-lon",lattitude +" " + longitude);
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(source);
                markerOptions.title("Current Position");

                if(source_dest_get == "source_current_location_imageview"){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));
                }
                else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b));
                }

                mCurrLocationMarker = mMap.addMarker(markerOptions);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);

            } else  if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.d("lat-lon",lattitude +" " + longitude);

                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(source);
                markerOptions.title("Current Position");
                if(source_dest_get == "source_current_location_imageview"){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));
                }
                else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b));
                }
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                //drawRoute(source,destination);

            } else  if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.d("lat-lon",lattitude +" " + longitude);

                //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(source);
                markerOptions.title("Current Position");
                if(source_dest_get == "source_current_location_imageview"){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));
                }
                else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b));
                }
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                //drawRoute(source,destination);

            }else{
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
            //Use Current Location Long Latt
            Log.i("latti..",latti+"");
            Log.i("longi..",longi+"");

            Log.d("latti-longi",latti +" " + longi);

            if(!Double.isNaN(latti) && !Double.isNaN(longi)){
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(latti, longi, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    Log.v("IGA", "Address" + add);
                    if(source_dest_get=="source_current_location_imageview"){
                        source_edittext.setText(add);
                        event.put("lat_source",latti+"");
                        event.put("lan_source",longi+"");
                        event_data.setLat_source(latti+"");
                        event_data.setLan_source(longi+"");
                    }else {
                        destination_edittext.setText(add);
                        event.put("lat_dest",longi+"");
                        event.put("lan_dest",latti+"");
                        event_data.setLat_dest(latti+"");
                        event_data.setLan_dest(longi+"");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void drawRoute(LatLng source, LatLng finalDestination, GoogleMap mMapLocal){

        try{
            if(source!=null && finalDestination!=null) {

                Log.i("DrawRoute","Came inside to drawa");

                LatLng origin = source;
                LatLng destination = finalDestination;
                DrawRouteMaps.getInstance(this)
                        .draw(origin, destination, mMapLocal);
                //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Origin Location");
                //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Destination Location");

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(origin)
                        .include(destination).build();
                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                mMapLocal.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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

        source_search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(source_edittext.getText())){
                    Toast.makeText(CreatePage2.this,"Please Enter Source Location.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String source_location = source_edittext.getText().toString().trim();
                destinationLocation(mMap,source_location,"source_search_imageview");

                LatLng source=null, destination=null;
                if(event.get("lat_source")!=null && event.get("lan_source")!=null)
                    source= new LatLng(Double.parseDouble(event.get("lan_source")),Double.parseDouble(event.get("lat_source")));

                if(event.get("lat_dest")!=null && event.get("lan_dest")!=null)
                    destination= new LatLng(Double.parseDouble(event.get("lan_dest")),Double.parseDouble(event.get("lat_dest")));

                drawRoute(source,destination,mMap);
            }
        });

        dest_search_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(destination_edittext.getText())){
                    Toast.makeText(CreatePage2.this,"Please Enter Destination Location.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String dest_location = destination_edittext.getText().toString().trim();
                destinationLocation(mMap,dest_location,"dest_search_imageview");

                LatLng source=null, destination=null;
                if(event.get("lat_source")!=null && event.get("lan_source")!=null)
                    source= new LatLng(Double.parseDouble(event.get("lan_source")),Double.parseDouble(event.get("lat_source")));

                if(event.get("lat_dest")!=null && event.get("lan_dest")!=null)
                    destination= new LatLng(Double.parseDouble(event.get("lan_dest")),Double.parseDouble(event.get("lat_dest")));

                //if(source!=null && destination!=null)
                    drawRoute(source,destination,mMap);
            }
        });

        add_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(event_name_edittext.getText())){
                    Toast.makeText(CreatePage2.this,"Please enter plan name.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(source_edittext.getText())){
                    Toast.makeText(CreatePage2.this,"Please enter source event location.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(destination_edittext.getText().toString().trim())){
                    Toast.makeText(CreatePage2.this,"Please enter destination event location.",Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean sourceFlag = false;
                boolean destFlag = false;

                if(event.get("lat_source")==null || event.get("lan_source")==null)
                    sourceFlag = true;

                if(event.get("lat_source")==null || event.get("lan_source")==null)
                    destFlag = true;

                if(!sourceFlag && !destFlag) {
                    String event_name = event_name_edittext.getText().toString().trim();
                    String source_location = source_edittext.getText().toString().trim();
                    String destination_location = destination_edittext.getText().toString().trim();
                    event.put("event_name", event_name);
                    event.put("event_source", source_location);
                    event.put("event_dest", destination_location);
                    event_data.setEvent_name(event_name);
                    event_data.setEvent_source(source_location);
                    event_data.setEvent_dest(source_location);
                    Intent intent1 = new Intent(CreatePage2.this, CreatePage3.class);
                    intent1.putExtra("hashmap", event);
                    startActivity(intent1);
                }else{
                    if(sourceFlag)
                        Toast.makeText(CreatePage2.this,"Please Enter the Source Location",Toast.LENGTH_SHORT).show();

                    if(destFlag)
                        Toast.makeText(CreatePage2.this,"Please Enter the Destination Location",Toast.LENGTH_SHORT).show();

                }
            }
        });

        source_current_location_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocaation("source_current_location_imageview");
            }
        });

        dest_current_location_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocaation("dest_current_location_imageview");
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
