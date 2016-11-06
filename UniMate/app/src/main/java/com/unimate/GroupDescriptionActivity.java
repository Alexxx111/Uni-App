package com.unimate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GroupDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_description);

        Intent intent = getIntent();
        String memberCountString = intent.getStringExtra("memberCount");

        TextView groupNameText = (TextView)findViewById(R.id.group_name_text);

        groupNameText.setText(memberCountString);
    }
}
