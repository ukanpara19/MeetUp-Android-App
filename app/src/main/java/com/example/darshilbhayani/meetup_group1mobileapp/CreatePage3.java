package com.example.darshilbhayani.meetup_group1mobileapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;

public class CreatePage3 extends AppCompatActivity{
    HashMap<String,String> event = new HashMap<>();
    static EditText date_edittext,time_edittext,duration_event;
    Button invite_friends;
    Event event_data = new Event();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_plan3);

        Intent intent = getIntent();
        event = (HashMap<String, String>)intent.getSerializableExtra("hashmap");

        date_edittext = findViewById(R.id.editText2);
        time_edittext = findViewById(R.id.editText3);
        duration_event = findViewById(R.id.editText4);


        time_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TimePicker mTimePicker = new TimePicker();
                    mTimePicker.show(getFragmentManager(), "Select time");
                }
            }
        });

        date_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerFragment mDatePicker = new DatePickerFragment();
                    mDatePicker.show(getFragmentManager(), "Select date");
                }
            }
        });

        invite_friends = findViewById(R.id.button4);

        invite_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(date_edittext.getText())){
                    Toast.makeText(CreatePage3.this,"Enter Date",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(time_edittext.getText())){
                    Toast.makeText(CreatePage3.this,"Enter Time",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(duration_event.getText())){
                    Toast.makeText(CreatePage3.this,"Enter Duration",Toast.LENGTH_SHORT).show();
                    return;
                }
                String duration = duration_event.getText().toString();
                String date = date_edittext.getText().toString().trim();
                String time = time_edittext.getText().toString().trim();

                int duration_int = Integer.valueOf(duration);
                if(duration_int > 1440){
                    Toast.makeText(CreatePage3.this,"Duration should be less than 24 hours.",Toast.LENGTH_SHORT).show();
                    return;
                }

                event.put("event_date",date);
                event.put("event_time",time);
                event.put("event_duration",duration +" minutes");


                event_data.setEvent_date(date);
                event_data.setEvent_time(time);
                event_data.setEvent_duration(String.valueOf(duration_int));

                Intent intent1 = new Intent(CreatePage3.this,requestContactPermission.class);
                intent1.putExtra("hashmap",event);
                startActivity(intent1);
            }
        });
    }
    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            time_edittext.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
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
                date_edittext.setText(String.valueOf(year) + "-" + String.valueOf("0" + (month + 1)) + "-" + String.valueOf("0" + day));
            }else if(day<10 & month>=10) {
                date_edittext.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf("0" + day));
            } else if (day>=10 & month<10) {
                date_edittext.setText(String.valueOf(year) + "-" + String.valueOf("0" + (month + 1)) + "-" + String.valueOf(day));
            }else {
                date_edittext.setText(String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day));
            }
        }
    }
}