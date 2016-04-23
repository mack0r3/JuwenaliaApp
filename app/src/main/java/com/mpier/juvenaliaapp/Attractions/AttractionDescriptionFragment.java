package com.mpier.juvenaliaapp.Attractions;
import com.mpier.juvenaliaapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionDescriptionFragment extends Fragment implements OnMapReadyCallback {

    View inflatedView;
    private GoogleMap mMap;

    public AttractionDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(inflatedView == null) inflatedView = inflater.inflate(R.layout.fragment_attraction_description, container, false);

        String attrName = "";
        String attrDesc = "";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            attrName = bundle.getString("attrName", "");
            attrDesc = bundle.getString("attrDesc", "");
        }

        TextView attractionName = (TextView)inflatedView.findViewById(R.id.attractionName);
        TextView attractionDescription = (TextView)inflatedView.findViewById(R.id.attractionDescription);

        attractionName.setText(attrName);
        attractionDescription.setText(attrDesc);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return inflatedView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng marker = new LatLng(52.2212022, 21.007445);
        mMap.addMarker(new MarkerOptions().position(marker).title("Politechnika Warszawska"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 17));
    }
}
