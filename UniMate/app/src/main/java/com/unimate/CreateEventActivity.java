package com.unimate;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unimate.model.Event;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private int startHour, startMinute, endHour, endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        Button createGroupButton = (Button) findViewById(R.id.create_event_button);
        final TextView groupNameText = (TextView)findViewById(R.id.event_name_text);
        final TextView groupDescritionText = (TextView) findViewById(R.id.group_description_text);
        final TextView startTime = (TextView)findViewById(R.id.start_time);
        final TextView endTime = (TextView)findViewById(R.id.end_time);
        //final TextClock endTime = (TextClock)findViewById(R.id.endClock);

        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);

        startTime.setText(String.valueOf(hour) + ":" +String.valueOf(minute));
        endTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute));

        startHour = hour;
        startMinute = minute;
        endHour = hour;
        endMinute = minute;

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                        startMinute = selectedMinute;
                        startHour = selectedHour;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                        endHour = selectedHour;
                        endMinute = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Event event = new Event();
                event.setName(groupNameText.getText().toString());
                event.setDescription(groupDescritionText.getText().toString());
                event.setGpsX(0f);
                event.setGpsY(0f);
                event.setMemberCount(1);
                event.setStartHour(startHour);
                event.setStartMinute(startMinute);
                event.setEndHour(endHour);
                event.setEndMinute(endMinute);

                mDatabase.child("events").child(groupNameText.getText().toString()).setValue(event);

                finish();
            }
        });

    }
}
