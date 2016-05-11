package com.mpier.juvenaliaapp.LineUp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpier.juvenaliaapp.R;

/**
 * Created by Selve on 2016-05-10.
 */
public class EventDescriptionActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineup_description);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String eventName = "", eventDesc = "";
        Integer eventImg = null;
        Bundle bundle = getIntent().getExtras();
        eventName = bundle.getString("eventName", "");
        eventImg = bundle.getInt("eventImg");
        eventDesc = bundle.getString("eventDesc", "");

        //TextView eventNameTextView = (TextView) findViewById(R.id.event_name);
        TextView eventDescriptionTextView = (TextView) findViewById(R.id.event_description);
        ImageView eventImageImageView = (ImageView) findViewById(R.id.event_img);

        //eventNameTextView.setText(eventName);
        getSupportActionBar().setTitle(eventName);
        eventDescriptionTextView.setText(eventDesc);
        eventImageImageView.setImageResource(eventImg);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
