package com.mpier.juvenaliaapp.Attractions;

import com.mpier.juvenaliaapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionsFragment extends Fragment {

    View inflatedView;
    Attraction attractions[];

    public AttractionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_attractions, container, false);

        initializeFragment();

        return inflatedView;
    }

    public void initializeFragment() {
        attractions = createAttractions();
        ListView attractionsList = createListViewForAttractions(attractions);
        attractionsList.setOnItemClickListener(onItemClickHandler);
    }

    AdapterView.OnItemClickListener onItemClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Fragment fragment = new AttractionDescriptionFragment();
            Bundle bundle = new Bundle();
            bundle.putString("attrName", attractions[position].getAttrName());
            bundle.putString("attrDesc", attractions[position].getDescription());
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        }
    };

    private Attraction[] createAttractions() {

        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nec nulla euismod, luctus dui vitae, fermentum arcu.";

        Attraction attractions[] = new Attraction[]{
                new Attraction("Stoisko Coca - Cola", R.drawable.cocacola, description),
                new Attraction("Gra we frisbee", R.drawable.cocacola, description),
                new Attraction("Stoisko mediów", R.drawable.cocacola, description),
                new Attraction("Hala piwna", R.drawable.cocacola, description),
                new Attraction("Namiot Juwenaliowy", R.drawable.cocacola, description),
                new Attraction("Stoisko OLX", R.drawable.cocacola, description),
                new Attraction("Stoisko Philip Morris", R.drawable.cocacola, description)
        };

        return attractions;
    }

    private ListView createListViewForAttractions(Attraction[] attractions) {

        ListAdapter myAdapter = new CustomAdapter(getContext(), R.layout.attraction_row, attractions);
        ListView attractionsList = (ListView) inflatedView.findViewById(R.id.attractionsList);
        attractionsList.setAdapter(myAdapter);
        return attractionsList;
    }


}
