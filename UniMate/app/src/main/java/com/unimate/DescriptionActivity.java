package com.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Event;

public class DescriptionActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        TextView descriptionText = (TextView)findViewById(R.id.description_text);
        TextView startTimeText = (TextView)findViewById(R.id.start_time_text);
        TextView endTimeText = (TextView)findViewById(R.id.end_time_text);
        TextView locationText = (TextView)findViewById(R.id.location_text);

//        Button joinGroupButton = (Button)findViewById(R.id.join_group_button);

        Intent intent = getIntent();
        String memberCountString = intent.getStringExtra("memberCount");
        final String groupLocationString = intent.getStringExtra("groupLocation");
        final String startTimeString = intent.getStringExtra("startTime");
        final String endTimeString = intent.getStringExtra("endTime");
        final String groupDescriptionString = intent.getStringExtra("groupDescription");
        final String groupNameString = intent.getStringExtra("groupName");

        setTitle(groupNameString);
        descriptionText.setText(groupDescriptionString);
        endTimeText.setText(endTimeString);
        startTimeText.setText(startTimeString);
        locationText.setText(groupLocationString);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add one to the membercounter:
                mDatabase.child("events").child(groupNameString).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Event e = dataSnapshot.getValue(Event.class);
                        int countBefore = e.getMemberCount();
                        e.setMemberCount(countBefore+1);
                        mDatabase.child("events").child(groupNameString).setValue(e);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent intent1 = new Intent(DescriptionActivity.this,  GroupActivity.class);

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
