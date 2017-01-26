package com.unimate;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Modul;
import com.unimate.model.User;

import java.util.ArrayList;

public class ChooseTagsActivity extends AppCompatActivity {

    private Spinner tag_spinner;

    private FirebaseAuth mAuth;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private FloatingActionButton proceed_button;

    private ChoiceAdapter choiceAdapter;

    private ListView modulesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_tags);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        modulesList = (ListView)findViewById(R.id.modules_list);

        final ArrayList<Modul> modules = new ArrayList<Modul>();

        tag_spinner = (Spinner)findViewById(R.id.tag_spinner);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        proceed_button = (FloatingActionButton) findViewById(R.id.proceed_button);

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: get all selected moduls and save them in the fb.

                User u = new User();
                u.setName(mAuth.getCurrentUser().getEmail().substring(0,4));
                u.setModules(choiceAdapter.getSelectedModules());

                mDatabase.child("users").child(mAuth.getCurrentUser().getEmail().substring(0,4)).setValue(u);

                Intent i = new Intent(ChooseTagsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

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
                    moduleSymbols.add(m.getSymbol());
                }

                choiceAdapter = new ChoiceAdapter(ChooseTagsActivity.this, modules, new ArrayList<Modul>());
                modulesList.setAdapter(choiceAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
