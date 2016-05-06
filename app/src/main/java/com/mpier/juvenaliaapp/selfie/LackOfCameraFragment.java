package com.mpier.juvenaliaapp.selfie;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpier.juvenaliaapp.R;

public class LackOfCameraFragment extends Fragment {


    public LackOfCameraFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lack_of_camera, container, false);
    }

}
