package com.mpier.juvenaliaapp.Attractions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mpier.juvenaliaapp.FragmentReplacer;
import com.mpier.juvenaliaapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionsFragment extends Fragment {

    View inflatedView;
    Attraction attractions[];
    AdapterView.OnItemClickListener onItemClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Fragment fragment = new AttractionDescriptionFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("attrImg", attractions[position].getAttrImgRes());
            bundle.putString("attrName", attractions[position].getAttrName());
            bundle.putString("attrDesc", attractions[position].getDescription());
            fragment.setArguments(bundle);
            FragmentReplacer.switchFragment(getFragmentManager(), fragment, true);
        }
    };

    public AttractionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_attractions);

        inflatedView = inflater.inflate(R.layout.fragment_attractions, container, false);

        initializeFragment();

        return inflatedView;
    }

    public void initializeFragment() {
        attractions = createAttractions();
        ListView attractionsList = createListViewForAttractions(attractions);
        attractionsList.setOnItemClickListener(onItemClickHandler);
    }

    private Attraction[] createAttractions() {

        String beerPongDescription = "Beer Pong to amerykańska, piwna gra, której początki sięgają lat 80-tych. Wszystko zaczęło się w akademikach, studenci dzielili się na dwie drużyny," +
                " ustawiali kubki na stołach do ping-ponga, nalewali do nich piwo a następnie rzucali do nich piłeczkami do ping-ponga. Jeżeli piłka wpadła do kubka którejś drużyny," +
                " drużyna ta musiała wypić jego zawartość.\n\nZ czasem gra ewoluowała, zaprojektowano specjalnego stoły do gry w beer ponga, powstały zasady turniejowe oraz co najważniejsze," +
                " Mistrzostwa Świata organizowane co roku w Las Vegas\n(http://www.bpong.com/wsobp/).\n\nW Polsce to jeszcze mało znany sport, ale możemy to zmienić, dlatego zapraszamy na strefę " +
                "Beer Ponga podczas tegorocznych Juwenaliów PW! Na strefie będą znajdować się dwa profesjonalne stoły oraz sprzęt do gry od BPONG.COM, czyli organizatora Mistrzostw Świata.";

        String philipMorrisDescription = "Szukajcie jak co roku stoiska Philipa Morrisa, który przygotuje dla Was całą masę niespodzianek.";

        String afterParty = "Afterparty, 13.05.2016 godz, 23:59, Klub Stodoła – Koniec piątkowych koncertów wcale nie oznacza  końca zabawy tego  dnia!  Czekamy  na Was w  Stodole,  gdzie tańce  i śpiewy  będą trwały do samego rana.";

        Attraction attractions[] = new Attraction[]{
                new Attraction("Beer Pong", R.drawable.beerpong, beerPongDescription),
                new Attraction("Stoisko Philip Morris", R.drawable.pmorris, philipMorrisDescription),
                new Attraction("Afterparty", R.drawable.stodola, afterParty)
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
