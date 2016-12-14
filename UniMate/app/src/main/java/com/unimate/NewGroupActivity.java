package com.unimate;

import android.content.Context;
import android.content.Intent;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimate.model.Message;

import java.util.ArrayList;

public class NewGroupActivity extends AppCompatActivity {

    MessageAdapter adapter;
    ListView listView;
    EditText messageEditText;
    FloatingActionButton fab;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private FirebaseAuth mAuth;

    private boolean textEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        fab = (FloatingActionButton) findViewById(R.id.fab);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);

        Intent intent = getIntent();
        String memberCountString = intent.getStringExtra("memberCount");
        final String startTimeString = intent.getStringExtra("startTime");
        final String endTimeString = intent.getStringExtra("endTime");
        final String groupDescriptionString = intent.getStringExtra("groupDescription");
        final String groupNameString = intent.getStringExtra("groupName");

        toolbar.setTitle(groupNameString);


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        final ArrayList<Message> messageList = new ArrayList<>();

        messageEditText = (EditText)findViewById(R.id.messageEditText);

        // pull messages from the server
        mDatabase.child("messages").child(groupNameString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messageList.clear();

                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Message m = d.getValue(Message.class);
                    messageList.add(m);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //change icon of floating button when text is entered
                if(messageEditText.getText().toString().length() > 0){
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_black_24dp));
                    textEntered = true;
                }
                else{
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_create_black_24dp));
                    textEntered = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textEntered){
                    DatabaseReference messagesRef = mDatabase.child("messages").child(groupNameString).push();

                    Message m = new Message();
                    m.setMessageText(messageEditText.getText().toString());
                    java.util.Date date= new java.util.Date();
                    // m.setTimestamp(new Timestamp(date.getTime()));
                    try{
                        m.setSender(mAuth.getCurrentUser().getEmail());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    messagesRef.setValue(m);

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    messageEditText.setText("");

                }
            }
        });

        ListView messageListView = (ListView)findViewById(R.id.message_list);

        // setup adapter
        adapter=new MessageAdapter(NewGroupActivity.this, messageList);
        messageListView.setAdapter(adapter);


    }
}
