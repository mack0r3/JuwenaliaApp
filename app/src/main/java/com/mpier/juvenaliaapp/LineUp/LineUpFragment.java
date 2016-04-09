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
    PagerTabStrip pagerTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View inflatedView = inflater.inflate(R.layout.fragment_lineup, container, false);

        viewPager = (ViewPager) inflatedView.findViewById(R.id.pager);

        events = createEvents();
        fragments = createFragments();

        pagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);

        pagerTabStrip = (PagerTabStrip) inflatedView.findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));

        return inflatedView;
    }

    private HashMap<String, ArrayList<Event>> createEvents() {
        final ArrayList<Event> fridayEvents = new ArrayList<Event>() {{
            add(new Event("16:15", "Bomba Kaloryczna", R.drawable.default_image));
            add(new Event("16:55", "The Cookies", R.drawable.default_image));
            add(new Event("17:40", "Mesajah", R.drawable.default_image));
            add(new Event("19:00", "Fisz Emade", R.drawable.default_image));
            add(new Event("20:50", "Sidney Polak", R.drawable.default_image));
            add(new Event("22:40", "Brodka", R.drawable.default_image));
        }};

        final ArrayList<Event> saturdayEvents = new ArrayList<Event>() {{
            add(new Event("16:15", "Pod Sufitem Dżungla", R.drawable.default_image));
            add(new Event("16:55", "Jutro Wieczorem", R.drawable.default_image));
            add(new Event("17:40", "Małpa", R.drawable.default_image));
            add(new Event("19:00", "The Dumpligs", R.drawable.default_image));
            add(new Event("20:20", "Lady Pank", R.drawable.default_image));
            add(new Event("22:00", "T.Love", R.drawable.default_image));
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
