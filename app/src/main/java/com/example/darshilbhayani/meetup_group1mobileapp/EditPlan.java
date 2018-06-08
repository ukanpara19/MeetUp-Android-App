package com.example.darshilbhayani.meetup_group1mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditPlan extends AppCompatActivity {

    DatabaseReference mDatabase;
    TextView planName, sourceLoc, destLoc, duration, eventType, time, date, invitedCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_plan);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        planName = (TextView) findViewById(R.id.planName);
        sourceLoc = (TextView) findViewById(R.id.sourceLoc);
        destLoc = (TextView) findViewById(R.id.destLoc);
        duration = (TextView) findViewById(R.id.duration);
        eventType = (TextView) findViewById(R.id.eventType);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        invitedCount = (TextView) findViewById(R.id.invitedCount);

        Event event = (Event) getIntent().getSerializableExtra("EVENT");
        String id = getIntent().getStringExtra("ID");



        Log.i("date",event.getEvent_date());

        Event event_set = new Event();
        event_set.setEvent_all("dbp3435@gmail.com",event.getEvent_date(),event.getEvent_dest(),
                event.getEvent_duration(), event.getEvent_name()+";",event.getEvent_source(),
                event.getEvent_time(),event.getEvent_type(),
                event.getLan_dest(),event.getLan_source(),event.getLat_dest(),event.getLat_source(),event.getppl_joined());
        mDatabase.child("event").child(id).setValue(event_set);

        Toast.makeText(getBaseContext(), "Updated Successfully!",
                Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, ListPlanDetails.class);
        startActivity(i);
    }
}

