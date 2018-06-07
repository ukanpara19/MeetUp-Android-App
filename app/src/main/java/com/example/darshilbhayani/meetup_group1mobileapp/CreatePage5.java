package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreatePage5 extends AppCompatActivity {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan5);

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
        Event event_set = new Event();
            event_set.setEvent_all("dbp3435@gmail.com",event.get("event_date"),event.get("event_dest"),event.get("event_duration"), event.get("event_name"),event.get("event_source"),
                    event.get("event_time"),event.get("event_type"),
                    event.get("lan_dest"),event.get("lan_source"),event.get("lat_dest"),event.get("lat_source"),"dhaval1019@yahoo.com");
            mDatabase.child("event").child("10").setValue(event_set);
        Toast.makeText(getBaseContext(), "Created Successfully!",
                Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(CreatePage5.this,MapsActivity.class);
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
}
