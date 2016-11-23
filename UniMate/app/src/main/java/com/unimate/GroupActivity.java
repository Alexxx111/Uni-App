package com.unimate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v4.widget.NestedScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Event;
import com.unimate.model.Message;

public class GroupActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private static LayoutInflater inflater=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //back button in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


        Intent intent = getIntent();
        String memberCountString = intent.getStringExtra("memberCount");
        final String startTimeString = intent.getStringExtra("startTime");
        final String endTimeString = intent.getStringExtra("endTime");
        final String groupDescriptionString = intent.getStringExtra("groupDescription");
        final String groupNameString = intent.getStringExtra("groupName");

        setTitle(groupNameString);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(GroupActivity.this, CreateMessageActivity.class);
                intent1.putExtra("groupName", groupNameString);
                startActivity(intent1);
            }
        });

        //define what the back button does
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // subtract one of the membercounter:
                mDatabase.child("events").child(groupNameString).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Event e = dataSnapshot.getValue(Event.class);
                        int countBefore = e.getMemberCount();
                        e.setMemberCount(countBefore-1);
                        mDatabase.child("events").child(groupNameString).setValue(e);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                finish();
            }
        });

        final LinearLayout ll = (LinearLayout)findViewById(R.id.linear_layout);

        // pull messages from the server
        mDatabase.child("messages").child(groupNameString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ll.removeAllViews();
                inflater = (LayoutInflater)GroupActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for(DataSnapshot d: dataSnapshot.getChildren()){

                    View vi = inflater.inflate(R.layout.message_element, null);
                    TextView message_text  = (TextView)vi.findViewById(R.id.message_text);

                    Message m = d.getValue(Message.class);
                    message_text.setText(m.getMessageText());

                    System.out.println("...-" + m.getMessageText());

                    ll.addView(vi);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
