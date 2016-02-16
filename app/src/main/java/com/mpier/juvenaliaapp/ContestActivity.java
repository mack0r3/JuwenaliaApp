package com.mpier.juvenaliaapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ContestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        final TextView textWithEventLink = (TextView) findViewById(R.id.textViewRule2);
        textWithEventLink.setMovementMethod(LinkMovementMethod.getInstance());
        textWithEventLink.setLinkTextColor(Color.BLUE);

        final TextView textWithRulesLink = (TextView) findViewById(R.id.textViewContent4);
        textWithRulesLink.setMovementMethod(LinkMovementMethod.getInstance());
        textWithRulesLink.setLinkTextColor(Color.BLUE);
    }
}
