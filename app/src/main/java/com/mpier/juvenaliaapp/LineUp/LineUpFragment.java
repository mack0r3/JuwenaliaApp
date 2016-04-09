package com.mpier.juvenaliaapp.LineUp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpier.juvenaliaapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Selve on 2016-04-08.
 */
public class LineUpFragment extends Fragment {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    ArrayList<Event> fridayEvents = new ArrayList<Event>() {{
        add(new Event("16:15", "Bomba Kaloryczna", R.drawable.default_image));
        add(new Event("16:55", "The Cookies", R.drawable.default_image));
        add(new Event("17:40", "Mesajah", R.drawable.default_image));
        add(new Event("19:00", "Fisz Emade", R.drawable.default_image));
        add(new Event("20:50", "Sidney Polak", R.drawable.default_image));
        add(new Event("22:40", "Brodka", R.drawable.default_image));
    }};

    ArrayList<Event> saturdayEvents = new ArrayList<Event>() {{
        add(new Event("16:15", "Pod Sufitem Dżungla", R.drawable.default_image));
        add(new Event("16:55", "Jutro Wieczorem", R.drawable.default_image));
        add(new Event("17:40", "Małpa", R.drawable.default_image));
        add(new Event("19:00", "The Dumpligs", R.drawable.default_image));
        add(new Event("20:20", "Lady Pank", R.drawable.default_image));
        add(new Event("22:00", "T.Love", R.drawable.default_image));
    }};

    ArrayList<ArrayList<Event>> events = new ArrayList<ArrayList<Event>>() {{
        add(fridayEvents);
        add(saturdayEvents);
    }};

    ArrayList<String> days = new ArrayList<String>() {{
        add("PIĄTEK");
        add("SOBOTA");
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View inflatedView = inflater.inflate(R.layout.fragment_lineup, container, false);

        viewPager = (ViewPager) inflatedView.findViewById(R.id.pager);

        List<EventsFragment> fragments = new ArrayList<EventsFragment>();

        for (int i = 0; i < events.size(); i++) {
            EventsFragment efragment = new EventsFragment();
            efragment.setFragment(days.get(i), events.get(i));
            fragments.add(efragment);
        }

        pagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) inflatedView.findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));

        return inflatedView;
    }
}
