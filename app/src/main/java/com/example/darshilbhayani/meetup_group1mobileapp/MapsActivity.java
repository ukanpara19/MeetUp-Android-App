package com.example.darshilbhayani.meetup_group1mobileapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

         //Drawable circleDrawable = getResources().getDrawable(R.drawable.common_google_signin_btn_icon_dark_focused);
         //BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        // Add a marker in Sydney and move the camera
        LatLng latLang = new LatLng(37.349957, -121.938827);
        mMap.addMarker(new MarkerOptions().position(latLang).title("Santa Clara Univeristy"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang,18),5000, null);

        List<Address> addresses;
        Geocoder geocoder = new Geocoder(this); 
        try {
            addresses = geocoder.getFromLocationName("Vilnius",5);

            Log.i("addresses..","."+addresses.size());

            if(addresses.size() > 0)
            {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

                Log.d("lat-long", "" + lat + "......." + lon);
                final LatLng user = new LatLng(lat, lon);

                /*Marker hamburg = mMap.addMarker(new MarkerOptions()
                        .position(user)
                        .title("2147 Newhall");
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.common_google_signin_btn_icon_dark_focused)));*/
                Marker hamburg = mMap.addMarker(new MarkerOptions()
                        .position(user)
                        .title("Vilnius"));
                // Move the camera instantly to hamburg with a zoom of 15.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 15));

                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
}
