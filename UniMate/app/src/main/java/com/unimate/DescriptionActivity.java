package com.unimate;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Event;

import com.oguzdev.circularfloatingactionmenu.library.*;

import java.util.Calendar;

import static android.R.id.button2;
import static com.unimate.R.drawable.ic_forum_black_24dp;
import static com.unimate.R.drawable.ic_today_black_24dp;

public class DescriptionActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

private String cameFromActivityString;

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

        cameFromActivityString = intent.getStringExtra("cameFromActivity");

        setTitle(groupNameString);
        descriptionText.setText(groupDescriptionString);
        endTimeText.setText(endTimeString);
        startTimeText.setText(startTimeString);
        locationText.setText(groupLocationString);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        int color= ContextCompat.getColor(this ,R.color.white);
        Drawable button= ContextCompat.getDrawable(this ,R.drawable.ic_brightness_1_fifth_24dp);
        Drawable icon2= ContextCompat.getDrawable(this, R.drawable.ic_today_black_24dp);
        Drawable icon=ContextCompat.getDrawable(this, ic_forum_black_24dp);
        if (Build.VERSION.SDK_INT >= 21) {
            icon.setTint(color);
            icon2.setTint(color);
        }
// Reminder:
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(icon2);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).setBackgroundDrawable(button).build();
// Chat:
        SubActionButton.Builder itemBuilder2 = new SubActionButton.Builder(this);
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(icon);
        SubActionButton button2 = itemBuilder2.setContentView(itemIcon2).setBackgroundDrawable(button).build();

// Create the menu with the items:

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .attachTo(fab)
                .build();
        button1.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Integer.parseInt(startTimeString.split(":")[0]), Integer.parseInt(startTimeString.split(":")[1]));
                Calendar endTime = Calendar.getInstance();
                endTime.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Integer.parseInt(endTimeString.split(":")[0]), Integer.parseInt(endTimeString.split(":")[1]));
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, groupNameString)
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Unimate-Event: "+groupDescriptionString)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, groupLocationString)
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
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

                Intent intent1 = new Intent(DescriptionActivity.this,  NewGroupActivity.class);

                intent1.putExtra("startTime", startTimeString);
                intent1.putExtra("endTime", endTimeString);
                intent1.putExtra("groupName", groupNameString);
                intent1.putExtra("groupDescription", groupDescriptionString);
                intent1.putExtra("groupLocation", groupLocationString);
                intent1.putExtra("cameFromActivity","description");


                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(cameFromActivityString!= null && (cameFromActivityString.equals("newGroup") || cameFromActivityString.equals("locationService"))){

            Intent intent = new Intent(DescriptionActivity.this, MainActivity.class);

            intent.putExtra("cameFromActivity","description");

            startActivity(intent);

        }
        else{
        super.onBackPressed();}
    }
}
