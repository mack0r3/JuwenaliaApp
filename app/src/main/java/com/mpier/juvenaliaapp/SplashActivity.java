package com.mpier.juvenaliaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ApplicationStartup().execute();
    }

    /**
     * AsyncTask used to preload application components before opening MainActivity with Fragments
     */
    private class ApplicationStartup extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // TODO: Do long app initialization instructions here

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Open MainActivity and finish SplashActivity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            // TODO: Face out transition
        }

    }
}
