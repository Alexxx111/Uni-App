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

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView descriptionText = (TextView)findViewById(R.id.description_text);
        TextView startTimeText = (TextView)findViewById(R.id.start_time_text);
        TextView endTimeText = (TextView)findViewById(R.id.end_time_text);

//        Button joinGroupButton = (Button)findViewById(R.id.join_group_button);

        Intent intent = getIntent();
        String memberCountString = intent.getStringExtra("memberCount");
        final String startTimeString = intent.getStringExtra("startTime");
        final String endTimeString = intent.getStringExtra("endTime");
        final String groupDescriptionString = intent.getStringExtra("groupDescription");
        final String groupNameString = intent.getStringExtra("groupName");

        setTitle(groupNameString);
        descriptionText.setText(groupDescriptionString);
        endTimeText.setText(endTimeString);
        startTimeText.setText(startTimeString);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(DescriptionActivity.this,  GroupActivity.class);

                intent1.putExtra("startTime", startTimeString);
                intent1.putExtra("endTime", endTimeString);
                intent1.putExtra("groupName", groupNameString);
                intent1.putExtra("groupDescription", groupDescriptionString);
                startActivity(intent1);
            }
        });
    }
}
