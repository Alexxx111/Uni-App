package com.unimate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unimate.model.Event;

public class CreateEventActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        Button create_event_button = (Button) findViewById(R.id.create_event_button);
        final TextView event_name_text = (TextView)findViewById(R.id.event_name_text);

        create_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Event event = new Event();
                event.setName(event_name_text.getText().toString());
                event.setGpsX(0f);
                event.setGpsY(0f);
                event.setMemberCount(1);

                mDatabase.child("events").child(event_name_text.getText().toString()).setValue(event);

                finish();
            }
        });

    }
}
