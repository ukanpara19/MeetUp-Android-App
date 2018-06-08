package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditPlan extends AppCompatActivity {

    DatabaseReference mDatabase;
    TextView eventType,planName,duration;
    TextView sourceLoc, destLoc;
    static  EditText eventDate, eventTime;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude,longitude;
    Marker mCurrLocationMarker;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    EditPlan  curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_plan);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        curr = this;

        planName = findViewById(R.id.planName);
        eventType = findViewById(R.id.eventType);
        sourceLoc = findViewById(R.id.sourceLoc);
        destLoc = findViewById(R.id.destLoc);
        duration = findViewById(R.id.duration);
        eventDate = findViewById(R.id.editText11);
        eventTime = findViewById(R.id.editText12);

        final Event event = (Event) getIntent().getSerializableExtra("EVENT");
        final String id = getIntent().getStringExtra("ID");

        Button btn = findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event_set = new Event();
                event_set.setEvent_all("dbp3435@gmail.com",eventDate.getText().toString().trim(),event.getEvent_dest(),
                        event.getEvent_duration(), event.getEvent_name()+";",event.getEvent_source(),
                        eventTime.getText().toString().trim(),event.getEvent_type(),
                        event.getLan_dest(),event.getLan_source(),event.getLat_dest(),event.getLat_source(),event.getppl_joined());
                mDatabase.child("event").child(id).setValue(event_set);

                Intent i = new Intent(curr, ListPlanDetails.class);
                startActivity(i);
            }
        });

        planName.setText(event.getEvent_type());
        eventType.setText(event.getEvent_name());
        sourceLoc.setText(event.getEvent_source());
        destLoc.setText(event.getEvent_dest());
        duration.setText(event.getEvent_duration());
        eventDate.setText(event.getEvent_date());
        eventTime.setText(event.getEvent_time());

        eventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    EditPlan.TimePicker mTimePicker = new TimePicker();
                    mTimePicker.show(getFragmentManager(), "Select time");
                }
            }
        });

        eventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    EditPlan.DatePickerFragment mDatePicker = new DatePickerFragment();
                    mDatePicker.show(getFragmentManager(), "Select date");
                }
            }
        });

       // drawRoute(latLng,latLng1);
    }

    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            eventTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            month = month + 1;
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(day<10 & month<10 ) {
                eventDate.setText(String.valueOf(year) + "-" + String.valueOf("0" + (month + 1)) + "-" + String.valueOf("0" + day));
            }else if(day<10 & month>=10) {
                eventDate.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf("0" + day));
            } else if (day>=10 & month<10) {
                eventDate.setText(String.valueOf(year) + "-" + String.valueOf("0" + (month + 1)) + "-" + String.valueOf(day));
            }else {
                eventDate.setText(String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day));
            }
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
}