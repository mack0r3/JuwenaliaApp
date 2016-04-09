package com.mpier.juvenaliaapp.LineUp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mpier.juvenaliaapp.R;

import java.util.ArrayList;

/**
 * Created by Selve on 2016-04-08.
 */
public class EventsFragment extends Fragment {

    private String day;
    private ArrayList<Event> events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_events, container, false);

        LineUpAdapter eventAdapter = new LineUpAdapter(this.getContext(), events);
        ListView listView = (ListView) inflatedView.findViewById(R.id.events_list_view);
        listView.setAdapter(eventAdapter);
        ListUtils.setDynamicHeight(listView);

        return inflatedView;
    }

    public void setFragment(String day, ArrayList<Event> events) {
        this.day = day;
        this.events = events;
    }

    public String getDay() {
        return this.day;
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
