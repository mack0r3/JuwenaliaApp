package com.mpier.juvenaliaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AttractionDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_description);

        Intent intent = getIntent();

        String attrName = intent.getStringExtra("attrName");
        String attrDesc = intent.getStringExtra("attrDesc");

        TextView attractionName = (TextView)findViewById(R.id.attractionName);
        TextView attractionDescription = (TextView)findViewById(R.id.attractionDescription);

        attractionName.setText(attrName);
        attractionDescription.setText(attrDesc);
    }
}
