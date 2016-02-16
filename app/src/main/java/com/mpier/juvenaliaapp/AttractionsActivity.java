package com.mpier.juvenaliaapp;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mpier.juvenaliaapp.AttractionsActivityTools.Attraction;
import com.mpier.juvenaliaapp.AttractionsActivityTools.CustomAdapter;

public class AttractionsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nec nulla euismod, luctus dui vitae, fermentum arcu. Ut dictum enim eu lacinia facilisis. Nunc maximus nibh magna, tempus commodo mi ultricies non. Praesent magna lorem, blandit ut nulla et, mollis aliquam elit.";


        final Attraction attractions[] = new Attraction[] {
            new Attraction("Stoisko Coca - Cola", R.drawable.cocacola, description),
            new Attraction("Gra we frisbee", R.drawable.cocacola, description),
            new Attraction("Stoisko mediów", R.drawable.cocacola, description),
            new Attraction("Hala piwna", R.drawable.cocacola, description),
            new Attraction("Namiot Juwenaliowy", R.drawable.cocacola, description),
            new Attraction("Stoisko OLX", R.drawable.cocacola, description),
            new Attraction("Stoisko Philip Morris", R.drawable.cocacola, description)
        };
        ListAdapter myAdapter = new CustomAdapter(getApplicationContext(), R.layout.attraction_row, attractions);
        ListView attractionsList = (ListView)findViewById(R.id.attractionsList);
        attractionsList.setAdapter(myAdapter);

        attractionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AttractionDescriptionActivity.class);
                intent.putExtra("attrName", attractions[position].getAttrName());
                intent.putExtra("attrDesc", attractions[position].getDescription());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.r_slide_in, R.anim.r_slide_out);
    }
}
