package com.mpier.juvenaliaapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mpier.juvenaliaapp.Attractions.Attraction;
import com.mpier.juvenaliaapp.Attractions.CustomAdapter;

/**
 * Created by mpier on 05-May-16.
 */
public class AboutFragment extends Fragment {

    public AboutFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_about);

        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
