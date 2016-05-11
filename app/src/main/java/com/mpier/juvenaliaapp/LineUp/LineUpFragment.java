package com.mpier.juvenaliaapp.LineUp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpier.juvenaliaapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Selve on 2016-04-08.
 */
public class LineUpFragment extends Fragment {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    HashMap<String, ArrayList<Event>> events;
    List<EventsFragment> fragments;
    //PagerTabStrip pagerTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        getActivity().setTitle(R.string.menu_line_up);

        View inflatedView = inflater.inflate(R.layout.fragment_lineup, container, false);

        events = createEvents();
        fragments = createFragments();

        pagerAdapter = new PagerAdapter(getFragmentManager(), fragments);

        viewPager = (ViewPager) inflatedView.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        //pagerTabStrip = (PagerTabStrip) inflatedView.findViewById(R.id.pager_tab_strip);
        //pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));

        return inflatedView;
    }

    private HashMap<String, ArrayList<Event>> createEvents() {
        final String default_text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce sed neque metus. Donec convallis, justo ac hendrerit accumsan, felis turpis commodo metus, id eleifend dolor lacus sodales arcu. Sed euismod consectetur dolor, eget dictum lacus egestas ac. Mauris dapibus blandit ex eget iaculis.";
        final ArrayList<Event> fridayEvents = new ArrayList<Event>() {{
            add(new Event("22:30", "Wilki", "Zespół został założony przez Roberta Gawlińskiego, w początkowym okresie przez formację przewinęło się wielu warszawskich muzyków, m.in. Andrzej Zeńczewski, Piotr Kokosiński, Robert Ochnio. 6 listopada 2012 roku nakładem wytwórni Sony Music został wydany siódmy album studyjny zespołu zatytułowany \"Światło i mrok\", który promował utwór „Czystego serca”. W lipcu 2014 zespół opuścił Stanisław Wróbel, po latach do zespołu powraca Marcin Ciempiel oraz syn Roberta- Beniamin Gawliński. 15 kwietnia 2016 roku ukazała się nowa płyta Wilków zatytułowana \"Przez dziewczyny\".", R.drawable.lineup_wilki));
            add(new Event("20:45", "O.S.T.R.", "Muzyk, producent, instrumentalista, raper. Jeden z najbardziej pracowitych i utalentowanych artystów w Polsce. Od kilkunastu lat konsekwentnie podąża w raz obranym kierunku artystycznym. Na scenie hiphopowej w kraju osiągnął właściwie wszystko.\nAutor 19 albumów muzycznych. Posiadacz 5 złotych i 4 platynowych płyt. Trzykrotny laureat Fryderyka Nagrody Akademii Fonograficznej. Od kilkunastu lat w nieprzerwanej trasie koncertowej. Jeden z pionierów w wykorzystaniu zespołu instrumentalistów w czasie koncertów hiphopowych w Polsce.", R.drawable.lineup_ostr));
            add(new Event("19:00", "Luxtorpeda", "Polska grupa wykonująca szeroko pojętą muzykę rockową. Powstała w 2010 roku z inicjatywy gitarzysty i wokalisty Roberta Friedricha, znanego z zespołów: Acid Drinkers, Arka Noego, Kazik na Żywo i 2Tm2,3. Muzyk do współpracy zaprosił związanych z zespołem 2Tm2,3 gitarzystę Roberta Drężka i basistę Krzysztofa Kmiecika oraz ówczesnego perkusistę Turbo i zespołu Armia Tomasza Krzyżaniaka. W 2011 roku w trakcie nagrywania albumu skład uzupełnił wokalista Przemysław „Hans” Frencel, raper znany z duetu Pięć Dwa.", R.drawable.lineup_luxtorpeda));
            add(new Event("17:45", "Poparzeni Kawą Trzy", "“Poparzeni Kawą Trzy\" to zespół przyjaciół, muzyków i dziennikarzy. Powstał w 2005 roku utworzony przez pracowników Radia RMF FM i Zetki oraz ich przyjaciół. Wydanie pierwszej płyty w 2011 roku zbiegło się ze spektakularnym sukcesem na \"Festiwalu Top Trendy\" w Sopocie gdzie zespół zdobył Nagrodę Dziennikarzy, a miesiąc później Zwyciężył na ogólnopolskim festiwalu \"Przebojem na antenę\". W 2013 roku ukazała się płyta CD ze wspomnianym DVD zatytułowana \"Wezmę cię\", obecnie zespół pracuje nad kolejnym krążkiem.\n\nSkład:\nRoman Osica, Krzysztof Zasada, Wojciech Jagielski, Mariusz Gierszewski, Krzyś Tomaszewski, Marian Hilla, Jacek Kret.", R.drawable.lineup_pk3));
            add(new Event("17:00", "Atom Heart", "Klasyczny kwartet grający energetyczny hard rock w nowoczesnym stylu.", R.drawable.lineup_atomheart));
        }};

        final ArrayList<Event> saturdayEvents = new ArrayList<Event>() {{
            add(new Event("22:30", "T.Love", "Polski zespół muzyczny, początkowo grający punk rocka, z biegiem czasu korzystający również ze stylistyki reggae, glam rocka, pop rocka czy rock and rolla. Większość ich albumów zyskało status złotych płyt, jeden z nich otrzymał w 2012 roku platynę. W swojej karierze występowali w prawie całej Europie, a także wielokrotnie w Stanach Zjednoczonych.", R.drawable.lineup_tlove));
            add(new Event("21:05", "Dawid Podsiadło", "Kiedy niespełna trzy lata temu szykował się do wydania pierwszej płyty, był dobrze zapowiadającym się zwycięzcą telewizyjnego talent show. Dziś jego debiutancki album „Comfort and Happiness” ciągle utrzymuje się w czołówce najlepiej sprzedających się płyt w Polsce z wynikiem ponad 160 000 tyś kopi a drugi, wydany pod koniec ubiegłego roku, solowy krążek pod przewrotnym tytułem „Annoyance and Disappointment” w 7 tygodni od premiery osiągnął status platynowy i także nie schodzi z list bestselerów. Dawid jest dziś bez wątpienia jedną z największych gwiazd polskiej muzyki popularnej, ciesząc się uznaniem zarówno krytyków jak i fanów.", R.drawable.lineup_dawidpodsiadlo));
            add(new Event("19:20", "Lao Cheo", "Polski crossoverowy zespół muzyczny, założony przez byłych członków zespołu Koli w 1999 roku, w Płocku. Nazwa zespołu pochodzi od Lao Che (\"Starego Che\"), jednej z drugoplanowych postaci filmu Indiana Jones i Świątynia Zagłady. Piąty album – Soundtrack został wydany 19 października 2012 r. Single promujące album to utwory pt. „Zombi!” i „Dym”. Trasa koncertowa Lao Che z najnowszą płytą rozpoczęła się 20 października 2012 r. od koncertu w Bydgoszczy. Od rozpoczęcia trasy zespół zagrał 32 koncerty promujące album.", R.drawable.lineup_laocheo));
            add(new Event("18:05", "Farben Lehre", "FARBEN LEHRE powstało we wrześniu 1986 roku. Od tamtego czasu ukazało się 11 płyt studyjnych: \"Bez pokory\", \"My maszyny\", \"Insekty\", \"Zdrada\", \"Atomowe zabawki\", \"Pozytywka\", \"Farbenheit\", \"Snukraina\", \"Ferajna\", \"Achtung 2012\", \"Projekt PUNK\", koncertowa \"Samo życie\". W grudniu 2013 nakładem Lou & Rocked Boys ukazał się - ostatni jak dotąd - album \"Projekt PUNK\", zawierający 20 klasyków polskiego punk-rock'a. W trackliście znalazły się, m.in utwory takich wykonawców jak: DEZERTER, ARMIA, REJESTRACJA, WC, MOSKWA, SIEKIERA, TILT czy KRYZYS.", R.drawable.lineup_farbenlehre));
            add(new Event("17:20", "Glass Ballerina", "Glass Ballerina to młody zespół ze Śląska, który w tym roku wydał swoją debiutancką płytę \"Stwory, zmory, potwory\". Ich brzmienie można określić jako indie rock, a teksty piosenek pisane są w naszym ojczystym języku. Najczęściej można ich usłyszeć na południu Polski, ale mają na swoim koncie występ m.in. na Woodstocku \u00AD na scenie Wiewiórstock, gdzie na swój koncert przyciągnęli mnóstwo festiwalowiczów, a w ostatnim czasie mieli okazję zagrać na Wiosna Fest w Krakowie, gdzie dzielili jedną scenę z Organkiem, Enejem, Luxtorpedą i happysad. Zespół rozwija się bardzo szybko, gdyż pomimo wydania pierwszej płyty w styczniu, w wakacje rozpoczynają nagrania kolejnego materiału.", R.drawable.lineup_glassballerina));
        }};

        HashMap<String, ArrayList<Event>> events = new HashMap<String, ArrayList<Event>>() {{
            put("SOBOTA", saturdayEvents);
            put("PIĄTEK", fridayEvents);
        }};

        return events;
    }

    private List<EventsFragment> createFragments() {
        List<EventsFragment> fragments = new ArrayList<EventsFragment>();

        for (Map.Entry<String, ArrayList<Event>> entry : events.entrySet()) {
            ArrayList<Event> day_events = entry.getValue();
            String day = entry.getKey();
            EventsFragment efragment = new EventsFragment();
            efragment.setFragment(day, day_events);
            fragments.add(efragment);
        }

        return fragments;
    }
}
