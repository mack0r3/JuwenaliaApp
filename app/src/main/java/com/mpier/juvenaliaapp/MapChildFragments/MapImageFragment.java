package com.mpier.juvenaliaapp.MapChildFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mpier.juvenaliaapp.R;

/**
 * Fragment containing map image
 *
 * Created by Konpon96 on 05-Apr-16.
 */
public class MapImageFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public MapImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_image, container, false);

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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);

        GroundOverlayOptions overlayOptions = new GroundOverlayOptions();
        overlayOptions.image(BitmapDescriptorFactory.fromBitmap(bitmap));
        overlayOptions.transparency(0.5f);
        LatLng southwest = new LatLng(52.211245, 21.008801);
        LatLng northeast = new LatLng(52.214225, 21.013685);
        overlayOptions.positionFromBounds(new LatLngBounds(southwest, northeast));

        final GroundOverlay overlay = mMap.addGroundOverlay(overlayOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(52.21293, 21.01146), 17f);

        mMap.moveCamera(cameraUpdate);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng latLng) {
                overlay.setVisible(!overlay.isVisible());
            }
        });

    }

}