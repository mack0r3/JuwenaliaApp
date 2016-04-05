package com.mpier.juvenaliaapp.MapChildFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mpier.juvenaliaapp.R;

/**
 * Fragment containing Google Maps with directions
 *
 * Created by Konpon96 on 05-Apr-16.
 */
public class MapDirectionsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public MapDirectionsFragment() {
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

        LatLng markerLatLng = new LatLng(52.2212022, 21.007445);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(markerLatLng)
                .title("Politechnika Warszawska"));

        marker.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
    }

}
