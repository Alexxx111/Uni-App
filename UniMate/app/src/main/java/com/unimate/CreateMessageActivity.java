package com.unimate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unimate.model.Message;

import java.sql.Timestamp;

public class CreateMessageActivity extends AppCompatActivity {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        Intent intent = getIntent();
        final String groupNameString = intent.getStringExtra("groupName");

        Button sendMessageButton = (Button)findViewById(R.id.send_message_button);
        final EditText messageText = (EditText)findViewById(R.id.message_text);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference messagesRef = mDatabase.child("messages").child(groupNameString).push();

                Message m = new Message();
                m.setMessageText(messageText.getText().toString());
                java.util.Date date= new java.util.Date();
               // m.setTimestamp(new Timestamp(date.getTime()));
                try{
                    m.setSender(mAuth.getCurrentUser().getEmail());
                }
                catch(Exception e){
                    e.printStackTrace();
                }

               messagesRef.setValue(m);

                finish();
            }
        });

    }
}
