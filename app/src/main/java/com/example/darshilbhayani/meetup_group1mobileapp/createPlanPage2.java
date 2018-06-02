package com.example.darshilbhayani.meetup_group1mobileapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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

public class createPlanPage2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan_page2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        final EditText tv_filter = (EditText) findViewById(R.id.editText);
        final Geocoder geocoder = new Geocoder(this);



        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (filterLongEnough()) {
                    Log.i("...","....Here in keyup!!");
                    List<Address> addresses;

                        try {
                            addresses = geocoder.getFromLocationName(tv_filter.getText().toString().trim(), 5);

                            Log.i("addresses..", "." + addresses.size());

                            if (addresses.size() > 0) {
                                Log.i("addresses..", "." + "Inside!!!"+tv_filter.getText().toString().trim());

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
            }

            private boolean filterLongEnough() {
                return tv_filter.getText().toString().trim().length() > 2;
            }
        };
        tv_filter.addTextChangedListener(fieldValidatorTextWatcher);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
