package com.mpier.juvenaliaapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TelebimFragment extends Fragment {

    public TelebimFragment() {}

    public static TelebimFragment newInstance() {
        return new TelebimFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        getActivity().setTitle(R.string.menu_telebim);

        return inflater.inflate(R.layout.fragment_telebim, container, false);
    }
}
