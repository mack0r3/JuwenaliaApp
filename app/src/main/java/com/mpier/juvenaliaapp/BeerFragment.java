package com.mpier.juvenaliaapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Selve on 2016-05-11.
 */
public class BeerFragment extends Fragment {

    public BeerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_beer);

        return inflater.inflate(R.layout.fragment_beer, container, false);
    }
}
