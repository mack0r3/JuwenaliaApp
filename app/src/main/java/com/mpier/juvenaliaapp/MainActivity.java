package com.mpier.juvenaliaapp;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Profile;
import com.mpier.juvenaliaapp.Attractions.AttractionsFragment;
import com.mpier.juvenaliaapp.LineUp.LineUpFragment;
import com.mpier.juvenaliaapp.selfie.SelfieFragment;

public class MainActivity extends AppCompatActivity implements FacebookLoginFragment.LoginResultListener, GoogleSigninFragment.LoginResultListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private String username = null;

    boolean isTilesFragment;

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

        if (savedInstanceState == null) {
            isTilesFragment = true;

            FragmentReplacer.switchToTiles(getSupportFragmentManager());
        }

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                isTilesFragment = false;
                //menuItem.setChecked(true);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setFragment(item);
                    }
                }, 300);
                //menuItem.setChecked(false);
                drawerLayout.closeDrawers();

                return false;
            }
        });

        handleHeaderOnClickEvent();

        ((AnalyticsApplication) getApplication()).getDefaultTracker();
    }

    private void handleHeaderOnClickEvent(){
        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentReplacer.switchToTiles(getSupportFragmentManager());
                //menuItem.setChecked(false);
                drawerLayout.closeDrawers();
            }
        });
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
    public void onFacebookLoginResult(boolean loginResult) {
        if(loginResult) {
            username = Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName();
        } else {
            username = null;
            new AlertDialog.Builder(this).setTitle("Facebook login").setMessage("Facebook login error").setNeutralButton("OK", null).show();
        }
    }

    @Override
    public void onGoogleLoginResult(boolean result, String name) {
        if(result) {
            username = name;
        } else {
            username = null;
            new AlertDialog.Builder(this).setTitle("Google login").setMessage("Google login error").setNeutralButton("OK", null).show();
        }
    }

    public void signIn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(dialogView);
        builder.show();
    }

    public void setIsTilesFragment(boolean isTilesFragment) {
        this.isTilesFragment = isTilesFragment;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (isTilesFragment) {
            super.onBackPressed();
        }
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                super.onBackPressed();
            }
            else {
                isTilesFragment = true;

                FragmentReplacer.switchToTiles(getSupportFragmentManager());
            }
        }
    }

    private void setFragment(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.menu_attractions:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), new AttractionsFragment(), false);
                break;
            case R.id.menu_beer:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), new BeerFragment(), false);
                break;
            case R.id.menu_line_up:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), new LineUpFragment(), false);
                break;
            case R.id.menu_selfie:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), new SelfieFragment(), false);
                break;
            case R.id.menu_map:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), new MapFragment(), false);
                break;
            case R.id.menu_telebim:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), TelebimFragment.newInstance(username), false);
                break;
            case R.id.menu_rules:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), new RulesFragment(), false);
                break;
            case R.id.menu_about:
                FragmentReplacer.switchFragment(getSupportFragmentManager(), new AboutFragment(), false);
                break;
        }
    }

}
