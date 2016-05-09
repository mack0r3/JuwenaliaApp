package com.mpier.juvenaliaapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Konpon96 on 06-May-16.
 */
public class RulesFragment extends Fragment {

    public RulesFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_rules);

        return inflater.inflate(R.layout.fragment_rules, container, false);
    }

}
