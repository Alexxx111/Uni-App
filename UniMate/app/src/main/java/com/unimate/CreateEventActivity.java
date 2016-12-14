package com.unimate;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Event;
import com.unimate.model.Modul;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private String[] spinnerArray;
    private int startHour, startMinute, endHour, endMinute;
    private double longitude, latitude ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        Button createGroupButton = (Button) findViewById(R.id.create_event_button);
        final TextView groupNameText = (TextView)findViewById(R.id.message_text);
        final TextView groupDescritionText = (TextView) findViewById(R.id.group_description_text);
        final TextView startTime = (TextView)findViewById(R.id.start_time);
        final TextView endTime = (TextView)findViewById(R.id.end_time);
        final EditText location_text = (EditText)findViewById(R.id.location_text);
        final Spinner tag_spinner = (Spinner) findViewById(R.id.tag_spinner);
        final Spinner semester_spinner = (Spinner) findViewById(R.id.semester_spinner);
        //final TextClock endTime = (TextClock)findViewById(R.id.endClock);

        //setup semester_spinner
        ArrayList<String> semesterStrings=new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            semesterStrings.add(String.valueOf(i));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, semesterStrings); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester_spinner.setAdapter(spinnerArrayAdapter);
        semester_spinner.setSelection(0, true);

        final ArrayList<Modul> modules = new ArrayList<Modul>();

        mDatabase.child("modul").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                modules.clear();
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    modules.add(d.getValue(Modul.class));
                    System.out.println("added modul");
                }

                ArrayList<String> moduleSymbols = new ArrayList<>();

                for(Modul m: modules){
                    int sem = 0;
                    sem = m.getSemester();
                    //auswahl nach semester filtern:
                    if(semester_spinner.getSelectedItem() != null) {
                        if (Integer.toString(sem).equals(semester_spinner.getSelectedItem().toString())) {
                            moduleSymbols.add(m.getSymbol());
                            System.out.println("added symbol" + m.getSymbol());
                        }
                    }

                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, moduleSymbols); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tag_spinner.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // #### auswahl neu setzen wenn anderes semester gesetzt wird:
        semester_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> moduleSymbols = new ArrayList<>();

                for(Modul m: modules){
                    int sem = 0;
                    sem = m.getSemester();
                    //auswahl nach semester filtern:
                    if(semester_spinner.getSelectedItem() != null) {
                        if (Integer.toString(sem).equals(semester_spinner.getSelectedItem().toString())) {
                            moduleSymbols.add(m.getSymbol());
                            System.out.println("added symbol" + m.getSymbol());
                        }
                    }

                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, moduleSymbols); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tag_spinner.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // ####

        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);

        if(minute < 10) {
            startTime.setText(String.valueOf(hour) + ":0" + String.valueOf(minute));
            endTime.setText(String.valueOf(hour) + ":0" + String.valueOf(minute));
        }else {
            startTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
            endTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
        }

        startHour = hour;
        startMinute = minute;
        endHour = hour;
        endMinute = minute;

        longitude =0d;
        latitude = 0d;

        //crate gpsTracker Object
        GPSTracker gpsTracker= new GPSTracker(CreateEventActivity.this);

        //if we can get gps ..
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude(); // returns latitude
            longitude =gpsTracker.getLongitude(); // returns longitude
        }
        else{
            gpsTracker.showSettingsAlert();
        }

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute < 10){
                            startTime.setText(String.valueOf(selectedHour) + ":0" + String.valueOf(selectedMinute));
                            endTime.setText(String.valueOf(selectedHour) + ":0" + String.valueOf(selectedMinute));
                        }else{
                            startTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                            endTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                        }
                        startMinute = selectedMinute;
                        startHour = selectedHour;

                        endHour = selectedHour;
                        endMinute = selectedMinute;
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
                        if(selectedMinute < 10){
                            endTime.setText(String.valueOf(selectedHour) + ":0" + String.valueOf(selectedMinute));
                        }else{
                            endTime.setText(String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                        }

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
                event.setLatitude(latitude);
                event.setLongitude(longitude);
                event.setMemberCount(1);
                event.setStartHour(startHour);
                event.setStartMinute(startMinute);
                event.setEndHour(endHour);
                event.setEndMinute(endMinute);
                event.setLocation(location_text.getText().toString());

                mDatabase.child("events").child(groupNameText.getText().toString()).setValue(event);

                String startTimeString = String.valueOf(startHour) + ":" + String.valueOf(startMinute);
                String endTimeString = String.valueOf(endHour) + ":" + String.valueOf(endMinute);
                String groupNameString = event.getName();
                String groupDescriptionString = event.getDescription();
                String groupLocationString = event.getLocation();
                // switch to group content activity
                Intent intent1 = new Intent(CreateEventActivity.this,  NewGroupActivity.class);

                intent1.putExtra("startTime", startTimeString);
                intent1.putExtra("endTime", endTimeString);
                intent1.putExtra("groupName", groupNameString);
                intent1.putExtra("groupDescription", groupDescriptionString);
                intent1.putExtra("groupLocation", groupLocationString);
                startActivity(intent1);
            }
        });

    }
}
