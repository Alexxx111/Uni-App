package com.unimate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Modul;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private TextView emailText;
    private ListView modulesList;
    private Button saveSettingsButton;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private ChoiceAdapter choiceAdapter;

    private ArrayList<Modul> alreadySelectedModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Settings");

        alreadySelectedModules = new ArrayList<>();

        modulesList = (ListView)findViewById(R.id.module_list_settings);

        Intent intent = getIntent();
        final String emailString = intent.getStringExtra("emailString");

        emailText = (TextView)findViewById(R.id.setting_email_text);
        saveSettingsButton = (Button)findViewById(R.id.save_settings_button);

        emailText.setText(emailString);

        //close the popup of the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(null, 0);

        final ArrayList<Modul> modules = new ArrayList<>();

        mDatabase.child("users").child(emailString.substring(0,4)).child("modules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Modul m = d.getValue(Modul.class);
                    alreadySelectedModules.add(m);
                }

                mDatabase.child("modul").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            Modul m = d.getValue(Modul.class);
                            modules.add(m);
                        }

                        System.out.println("alreadySelected: " + alreadySelectedModules);

                        choiceAdapter = new ChoiceAdapter(SettingsActivity.this, modules, alreadySelectedModules);
                        modulesList.setAdapter(choiceAdapter);
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



        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(emailString.substring(0,4)).child("modules").setValue(choiceAdapter.getSelectedModules());
                finish();
            }
        });

    }
}
