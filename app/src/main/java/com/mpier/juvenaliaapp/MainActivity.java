package com.mpier.juvenaliaapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.login.LoginResult;
import com.mpier.juvenaliaapp.LineUp.LineUpFragment;

public class MainActivity extends AppCompatActivity implements FacebookLoginFragment.LoginResultListener, GoogleSigninFragment.LoginResultListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        FacebookLoginFragment.newInstance();

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                item.setChecked(true);
                drawerLayout.closeDrawers();
                Intent intent;
                switch(item.getItemId())
                {
                    case R.id.menu_attractions:
                        intent = new Intent(getApplicationContext(), AttractionsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_line_up:
                        FragmentTransaction lineUpTransaction = getSupportFragmentManager().beginTransaction();
                        lineUpTransaction.replace(R.id.main_container, new LineUpFragment());
                        lineUpTransaction.commit();
                        break;
                    case R.id.menu_selfie: {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new SelfieFragment());
                        fragmentTransaction.commit();
                        break;
                    }
                    case R.id.menu_map: {
                        intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.menu_telebim:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, TelebimFragment.newInstance()).commit();
                        break;
                }
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                item.setChecked(false);
                return false;
            }
        });
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFacebookLoginResult(LoginResult loginResult) {
        if(loginResult == null) {
            new AlertDialog.Builder(this).setTitle("Facebook login").setMessage("Facebook login error").setNeutralButton("OK", null).show();
        }
    }

    @Override
    public void onGoogleLoginResult(String text) {
        new AlertDialog.Builder(this).setTitle("Google login").setMessage(text).setNeutralButton("OK", null).show();
    }

    public void signIn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(dialogView);
        builder.show();
    }
}
