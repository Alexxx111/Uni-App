package com.unimate;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Event;
import com.unimate.model.Modul;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class FeedActivity extends Fragment {

    private FirebaseAuth mAuth;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private ListAdapter adapter;

    private ArrayList<Modul> alreadySelectedModules;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //reloadList();

        return inflater.inflate(R.layout.activity_feed, container, false);



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        listView = (ListView)getView().findViewById(R.id.listView);

        reloadList();

        FloatingActionButton myFab = (FloatingActionButton)getView().findViewById(R.id.myFAB);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent  i = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(i);
            }
        });

    }

    public void reloadList(){
        alreadySelectedModules = new ArrayList<>();


        //load list start ----

        final ArrayList events = new ArrayList<Event>();

        mDatabase.child("users").child(mAuth.getCurrentUser().getEmail().substring(0,4)).child("modules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                alreadySelectedModules.clear();

                for(DataSnapshot d1 : dataSnapshot.getChildren()){
                    Modul m = d1.getValue(Modul.class);
                    alreadySelectedModules.add(m);
                }

                mDatabase.child("events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        events.clear();

                        for(DataSnapshot d: dataSnapshot.getChildren()){
                            System.out.println("hallowww " + d.getValue().toString());

                            Event e= d.getValue(Event.class);

                            for(Modul alreadySelectedModule : alreadySelectedModules){

                                System.out.println("eventName: " + e.getModulName()+ " , already: " + alreadySelectedModule.getName());

                                if(alreadySelectedModule.getName().equals(e.getModulName())){
                                    events.add(e);
                                }
                            }


                        }
                        // setup adapter
                        adapter=new ListAdapter(getActivity(), events);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                Event e = (Event) adapterView.getAdapter().getItem(i);
                                String groupNameString = e.getName();
                                String startTimeString = e.getStartHour() + ":" + e.getStartMinute();
                                String endTimeString = e.getEndHour() + ":" + e.getEndMinute();
                                intent.putExtra("startTime", startTimeString);
                                intent.putExtra("endTime", endTimeString);
                                intent.putExtra("groupName", groupNameString);
                                intent.putExtra("groupLocation", e.getLocation());
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // load list end ----

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

    @Override
    public void onStart() {
        super.onStart();

        reloadList();
        System.out.println("onStrat called");
    }

    private void onBackPressed() {
    }
}