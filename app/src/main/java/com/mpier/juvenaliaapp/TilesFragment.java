package com.mpier.juvenaliaapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpier.juvenaliaapp.Attractions.AttractionsFragment;
import com.mpier.juvenaliaapp.LineUp.LineUpFragment;
import com.mpier.juvenaliaapp.selfie.SelfieFragment;

import java.util.ArrayList;
import java.util.List;


public class TilesFragment extends Fragment {

    View inflatedView;

    public TilesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_tiles, container, false);

        final List<Pair<View, Fragment>> associatedTilesArrayWithFragments = createAssociatedTilesArrayWithFragments();
        startFragmentOnTileClicked(associatedTilesArrayWithFragments);

        return inflatedView;
    }

    private List<Pair<View, Fragment>> createAssociatedTilesArrayWithFragments()
    {
        List<Pair<View, Fragment>> associatedTilesArrayWithFragment = new ArrayList<>();
        addViewFragmentPairToList(associatedTilesArrayWithFragment,
                inflatedView.findViewById(R.id.lineUpTile),
                new LineUpFragment());
        addViewFragmentPairToList(associatedTilesArrayWithFragment,
                inflatedView.findViewById(R.id.telebimTile),
                new TelebimFragment());
        addViewFragmentPairToList(associatedTilesArrayWithFragment,
                inflatedView.findViewById(R.id.selfieTile),
                new SelfieFragment());
        addViewFragmentPairToList(associatedTilesArrayWithFragment,
                inflatedView.findViewById(R.id.attractionsTile),
                new AttractionsFragment());
        addViewFragmentPairToList(associatedTilesArrayWithFragment,
                inflatedView.findViewById(R.id.mapTile),
                new MapFragment());

        return associatedTilesArrayWithFragment;
    }

    private void addViewFragmentPairToList(List<Pair<View, Fragment>> list, View view, Fragment fragment){
        Pair pair = new Pair(view, fragment);
        list.add(pair);
    }

    private void startFragmentOnTileClicked(List<Pair<View, Fragment>> list){
        for(int i = 0; i < list.size(); i++)
        {
            final Fragment fragment = list.get(i).getFragment();
            View view = list.get(i).getView();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.setIsTilesFragment(false);

                    FragmentReplacer.switchFragment(getFragmentManager(), fragment);
                }
            });
        }
    }
}
class Pair<View, Fragment>{
    private View view;
    private Fragment fragment;

    public Pair(View view, Fragment fragment){
        this.view = view;
        this.fragment = fragment;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
