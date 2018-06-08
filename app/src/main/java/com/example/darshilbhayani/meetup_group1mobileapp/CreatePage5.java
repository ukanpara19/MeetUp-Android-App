package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class CreatePage5 extends AppCompatActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    HashMap<String, String> event = new HashMap<>();
    HashMap<String, String> invited_people = new HashMap<>();

    TextView planName, sourceLoc, destLoc, duration, eventType, time, date, invitedCount;

    ImageView imageID;
    CardView cardView;
    Button createEvent;
    DatabaseReference mDatabase;
    Context context;
    View v;

    private GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan5);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MapsActivity.REQUEST_LOCATION);

        planName = (TextView) findViewById(R.id.planName);
        sourceLoc = (TextView) findViewById(R.id.sourceLoc);
        destLoc = (TextView) findViewById(R.id.destLoc);
        duration = (TextView) findViewById(R.id.duration);
        eventType = (TextView) findViewById(R.id.eventType);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        invitedCount = (TextView) findViewById(R.id.invitedCount);

        cardView = (CardView) findViewById(R.id.cardView1);
        imageID = (ImageView) findViewById(R.id.imageID);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Intent intent = getIntent();
        event = (HashMap<String, String>) intent.getSerializableExtra("hashmap");
        invited_people = (HashMap<String, String>) intent.getSerializableExtra("hashmapofinvitedpeople");

        Log.d("data1",event+"");

        planName.setText(event.get("event_name"));
        sourceLoc.setText(event.get("event_source"));
        destLoc.setText(event.get("event_dest"));
        duration.setText(event.get("event_duration"));
        eventType.setText(event.get("event_type"));
        time.setText(event.get("event_time"));
        date.setText(event.get("event_date"));

        Log.d("data2",invited_people+"");

        invitedCount.setText(String.valueOf(invited_people.size()));

        Log.i("DARTTTAAA...",event.get("event_type"));

        if(event.get("event_type").equals("Entertainment")) {
            Log.i("In","--22-");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageID.setImageResource(R.drawable.movie);
                imageID.setImageDrawable(getResources().getDrawable(R.drawable.movie));
             } else {
                imageID.setImageDrawable(getResources().getDrawable(R.drawable.movie));
                imageID.setImageResource(R.drawable.movie);
            }
          //  imageID.setImageResource(R.drawable.movie);
        }
        else if(event.get("event_type").equals("Food")) {
            Log.i("In","--11-");
            imageID.setImageResource(R.drawable.food_gray);
        }
        else if(event.get("event_type").equals("Sports")) {
            Log.i("In","---");

        }
        else if(event.get("event_type").equals("Study")) {
            Log.i("In","--44-");
            imageID.setImageResource(R.drawable.book);
        }
        else if(event.get("event_type").equals("Carpool")) {
            Log.i("In","--55-");
            imageID.setImageResource(R.drawable.car);
        }
        else if(event.get("event_type").equals("Other")) {
            Log.i("In","-66--");
            imageID.setImageResource(R.drawable.other);
        }

        createEvent = findViewById(R.id.button4);

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMessagePermission();
                insertData();
            }
        });
    }

    private void requestMessagePermission() {
        if (ContextCompat.checkSelfPermission(CreatePage5.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePage5.this,Manifest.permission.SEND_SMS)) {
            }
            else {
                ActivityCompat.requestPermissions(CreatePage5.this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Iterator it = invited_people.entrySet().iterator();
                    while (it.hasNext()){
                        Map.Entry pair = (Map.Entry)it.next();
                        MultipleSMS(pair.getValue().toString(),pair.getKey().toString());
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
    private void insertData() {
        Random ran = new Random();
        Event event_set = new Event();

        SharedPreferences editor = getApplicationContext().getSharedPreferences(LoginDemo.MY_PREFS_NAME, MODE_PRIVATE);
        String loogedInUser = editor.getString("Email_ID","darshilbhayani1992@gmail.com");

            event_set.setEvent_all(loogedInUser,event.get("event_date"),event.get("event_dest"),event.get("event_duration"), event.get("event_name"),event.get("event_source"),
                    event.get("event_time"),event.get("event_type"),
                    event.get("lan_dest"),event.get("lan_source"),event.get("lat_dest")
                    ,event.get("lat_source"),"");
            mDatabase.child("event").child(String.valueOf(ran.nextInt())).setValue(event_set);

        Toast.makeText(getBaseContext(), "Created Successfully!",
                Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(CreatePage5.this,MapsActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);

    }


    private void MultipleSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ContentValues values = new ContentValues();
                        Iterator it = event.entrySet().iterator();
                        while (it.hasNext()){
                            Map.Entry pair = (Map.Entry)it.next();
                            values.put("address",pair.getValue().toString());
                            values.put("body",pair.getKey().toString()+" Hello How are Khana Kha k jana");
                        }
                        getContentResolver().insert(
                                Uri.parse("content://sms/sent"), values);
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng source = null;
        LatLng destination = null;

        if(event.get("lat_source")!=null && event.get("lan_source")!=null) {
            source = new LatLng(Double.parseDouble(event.get("lat_source")), Double.parseDouble(event.get("lan_source")));
            mMap.addMarker(new MarkerOptions().position(source).title("Start Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a)));
        }

        if(event.get("lan_dest")!=null && event.get("lat_dest")!=null) {
            destination = new LatLng(Double.parseDouble(event.get("lan_dest")), Double.parseDouble(event.get("lat_dest")));
            mMap.addMarker(new MarkerOptions().position(destination).title("Destination Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_b)));
        }

        if(source!=null && destination!=null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 18), 5000, null);
            drawRoute(source, destination);
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
