package com.mpier.juvenaliaapp.MapChildFragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mpier.juvenaliaapp.R;

/**
 * Fragment containing Google Maps with directions
 * <p>
 * Created by Konpon96 on 05-Apr-16.
 */
public class MapDirectionsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int APP_PERMISSION_ACCESS_FINE_LOCATION = 1;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    public MapDirectionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_directions, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                updateMap(true);
                return true;
            }
        });

        // Android 6.0+ permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // User denied permission request earlier or in system settings - disable location layer
                setMyLocationEnabled(false);
            } else {
                // User didn't grant nor deny permission earlier - request permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        APP_PERMISSION_ACCESS_FINE_LOCATION);
            }

        } else {
            // Permission was granted earlier - enable location layer
            setMyLocationEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case APP_PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted - enable location layer
                    setMyLocationEnabled(true);
                }
                break;
            }
        }

    }

    /**
     * Method wrapper, catching a SecurityException
     *
     * @param enabled If to enable location layer on Google Map
     */
    private void setMyLocationEnabled(boolean enabled) {
        try {
            mMap.setMyLocationEnabled(enabled);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method wrapper, catching a SecurityException
     *
     * @return Current user location or null in case of failure
     */
    private Location getLastLocation() {
        try {
            return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update Google Map
     *
     * @param animate If to animate updating the map
     */
    private void updateMap(boolean animate) {

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        // Main marker
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(52.2212022, 21.007445))
                .title("Politechnika Warszawska"));
        boundsBuilder.include(marker.getPosition());

        // User location (if location is enabled)
        Location lastLocation = null;
        if (mMap.isMyLocationEnabled() && mGoogleApiClient.isConnected()) {
            // Get user location and add to bounds builder
            lastLocation = getLastLocation();
            if (lastLocation != null) {
                boundsBuilder.include(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
            }
        }

        // Move the map
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),
                getResources().getDimensionPixelOffset(R.dimen.map_bounds_margin));

        if (animate) {
            mMap.animateCamera(cameraUpdate);
            if (!mMap.isMyLocationEnabled() || lastLocation == null) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            }
        } else {
            mMap.moveCamera(cameraUpdate);
            if (!mMap.isMyLocationEnabled() || lastLocation == null) {
                mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            }
        }

        marker.showInfoWindow();

    }

    @Override
    public void onConnected(Bundle bundle) {
        updateMap(false);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        updateMap(false);
    }
}
