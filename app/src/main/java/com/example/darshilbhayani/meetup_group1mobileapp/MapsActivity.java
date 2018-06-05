package com.example.darshilbhayani.meetup_group1mobileapp;

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
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

//import com.example.drawroutemap.DrawRouteMaps;

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
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 1;
    private static final String IPKey = "AIzaSyAkJ_dRjWP9-g34GiloedGp1H_pUBIH6ME";

    Button button;
    TextView textView;
    LocationManager locationManager;
    String lattitude,longitude;

    LatLng destination;
    LatLng source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

//        textView = (TextView)findViewById(R.id.text_location);
//        button = (Button)findViewById(R.id.button_location);

        button.setOnClickListener(this);
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
        mMap = googleMap;

        sourceLocation(mMap);
        destinationLocation(mMap);

        drawRoute(source,destination);

        /*mMap = googleMap;

        LatLng barcelona = new LatLng(41.385064,2.173403);
        mMap.addMarker(new MarkerOptions().position(barcelona).title("Marker in Barcelona"));

        LatLng madrid = new LatLng(40.416775,-3.70379);
        mMap.addMarker(new MarkerOptions().position(madrid).title("Marker in Madrid"));

        LatLng zaragoza = new LatLng(41.648823,-0.889085);

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(IPKey)
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.385064,2.173403", "40.416775,-3.70379");
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("ERROR!!", ex.getLocalizedMessage());
        }

        Log.i("...path.size()...","-"+path.size());

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 6));*/
    }

    private void destinationLocation(GoogleMap mMap) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this);
        try {
            // addresses = geocoder.getFromLocationName("Vilnius",5);
            addresses = geocoder.getFromLocationName("Jersey Shore, New Jersey",5);

            Log.i("addresses..","."+addresses.size());

            if(addresses.size() > 0)
            {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

                Log.d("lat-long", "" + lat + "......." + lon);
                destination = new LatLng(lat, lon);

                /*Marker hamburg = mMap.addMarker(new MarkerOptions()
                        .position(user)
                        .title("2147 Newhall");
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.common_google_signin_btn_icon_dark_focused)));*/
                Marker hamburg = mMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .title("Jersey Shore, New Jersey").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b)));
                // Move the camera instantly to hamburg with a zoom of 15.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));

                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void sourceLocation(GoogleMap mMap) {
        // Add a marker in Sydney and move the camera
        //LatLng latLang = new LatLng(37.349957, -121.938827);
        source = new LatLng(30.332523, -97.844303);

        //  mMap.addMarker(new MarkerOptions().position(latLang).title("Santa Clara Univeristy"));
        mMap.addMarker(new MarkerOptions().position(source).title("Lake Austin").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
    }

    public void drawRoute(LatLng source, LatLng finalDestination){
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

    /*
     // Getting Google Play availability status
     int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

     // Showing status
     if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

         int requestCode = 10;
         Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
         dialog.show();

     }else { // Google Play Services are available

         Log.i("Demo!!","....................");

         // Getting reference to the SupportMapFragment of activity_main.xml
         SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

         // Enabling MyLocation Layer of Google Map
         //mMap.setMyLocationEnabled(true);

         // Opening the sharedPreferences object
         sharedPreferences = getSharedPreferences("location", 0);

         // Getting number of locations already stored
         locationCount = sharedPreferences.getInt("locationCount", 0);

         // Getting stored zoom level if exists else return 0
         String zoom = sharedPreferences.getString("zoom", "0");

         // If locations are already saved

             String lat = "";
             String lng = "";

             // Iterating through all the locations stored
             for(int i=0;i<3;i++){

                 // Getting the latitude of the i-th location
                 lat = sharedPreferences.getString("lat"+i,"0");

                 // Getting the longitude of the i-th location
                 lng = sharedPreferences.getString("lng"+i,"0");

                 // Drawing marker on the map
                 drawMarker(i,new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                 Log.i("Demo!!","............11........");
             }

             // Moving CameraPosition to last clicked position
         mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
             // Setting the zoom level in the map on last position  is clicked
         mMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));
         }

     mMap.setOnMapClickListener(new OnMapClickListener() {

         @Override
         public void onMapClick(LatLng point) {
             locationCount++;

             // Drawing marker on the map
             drawMarker(0, point);

             // Opening the editor object to write data to sharedPreferences
             SharedPreferences.Editor editor = sharedPreferences.edit();

             // Storing the latitude for the i-th location
             editor.putString("lat"+ Integer.toString((locationCount-1)), Double.toString(point.latitude));

             // Storing the longitude for the i-th location
             editor.putString("lng"+ Integer.toString((locationCount-1)), Double.toString(point.longitude));

             // Storing the count of locations or marker count
             editor.putInt("locationCount", locationCount);

             // Storing the zoom level to the shared preferences
             editor.putString("zoom", Float.toString(mMap.getCameraPosition().zoom));

             // Saving the values stored in the shared preferences
             editor.commit();

             Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();

         }
     });

     mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
         @Override
         public void onMapLongClick(LatLng point) {

             // Removing the marker and circle from the Google Map
             mMap.clear();

             // Opening the editor object to delete data from sharedPreferences
             SharedPreferences.Editor editor = sharedPreferences.edit();

             // Clearing the editor
             editor.clear();

             // Committing the changes
             editor.commit();

             // Setting locationCount to zero
             locationCount=0;

         }
     });
     */

/* private void drawMarker(int i, LatLng point){
     // Creating an instance of MarkerOptions
     MarkerOptions markerOptions = new MarkerOptions();

     // Setting latitude and longitude for the marker
     markerOptions.position(point);
     markerOptions.title("Testing Data "+i);
     if(i==0)
         markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
     else if(i==1)
         markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
     else
         markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

     // Adding marker on the Google Map
     mMap.addMarker(markerOptions);
 } */



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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                mMap.addMarker(new MarkerOptions().position(source).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                drawRoute(source,destination);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                mMap.addMarker(new MarkerOptions().position(source).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                drawRoute(source,destination);


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                //textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude + "\n" + "Longitude = " + longitude);
                LatLng source = new LatLng(Double.parseDouble(lattitude), Double.parseDouble(longitude));
                mMap.addMarker(new MarkerOptions().position(source).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source,18),5000, null);
                drawRoute(source,destination);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

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
    }
}
