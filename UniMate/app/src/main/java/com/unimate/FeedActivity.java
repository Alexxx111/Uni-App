package com.unimate;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Event;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class FeedActivity extends BaseActivity {

    private FirebaseAuth mAuth;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private Toolbar toolbar;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        Button logout_button = (Button) findViewById(R.id.logout_button);
        final ListView listView = (ListView)findViewById(R.id.listView);

        //load list start ----

        final ArrayList events = new ArrayList<Event>();

        mDatabase.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                events.clear();

                for(DataSnapshot d: dataSnapshot.getChildren()){
                    events.add(d.getValue(Event.class));
                }
                // setup adapter
                adapter=new ListAdapter(FeedActivity.this, events);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(FeedActivity.this, DescriptionActivity.class);
                        Event e = (Event) adapterView.getAdapter().getItem(i);
                        String groupNameString = e.getName();
                        intent.putExtra("groupName", groupNameString);
                        String groupDescriptionString = e.getDescription();
                        intent.putExtra("groupDescription", groupDescriptionString);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // load list end ----

        ArrayList names = new ArrayList<String>();
        names.add("testname");
        ArrayList count = new ArrayList<String>();
        count.add("1");

        System.out.println("--- " + names.size() + " ,, " + count.size());



        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent i = new Intent(FeedActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent  i = new Intent(FeedActivity.this, CreateEventActivity.class);
                startActivity(i);
            }
        });


    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
