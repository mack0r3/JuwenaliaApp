package com.mpier.juvenaliaapp.MapChildFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpier.juvenaliaapp.R;

/**
 * Fragment containing ImageView with map
 *
 * Created by Konpon96 on 05-Apr-16.
 */
public class MapImageFragment extends Fragment {

    public MapImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_map, container, false);
    }

}