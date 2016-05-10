package com.mpier.juvenaliaapp.LineUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mpier.juvenaliaapp.FragmentReplacer;
import com.mpier.juvenaliaapp.R;
import com.mpier.juvenaliaapp.TelebimFragment;

import java.util.ArrayList;

/**
 * Created by Selve on 2016-04-08.
 */
public class EventsFragment extends Fragment {

    private String day;
    private ArrayList<Event> events = new ArrayList<Event>();
    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_events, container, false);

        if(savedInstanceState != null) {
            events = (ArrayList<Event>) savedInstanceState.getSerializable("events");
        }

        setListView();

        return inflatedView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("events", events);
    }

    public void setFragment(String day, ArrayList<Event> events) {
        this.day = day;
        this.events = events;
    }

    public String getDay() {
        return this.day;
    }

    private void setListView() {
        LineUpAdapter eventAdapter = new LineUpAdapter(this.getContext(), events);
        ListView listView = (ListView) inflatedView.findViewById(R.id.events_list_view);
        listView.setAdapter(eventAdapter);
        ListUtils.setDynamicHeight(listView);
        listView.setOnItemClickListener(onItemClickHandler);
    }

    AdapterView.OnItemClickListener onItemClickHandler = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Fragment fragment = new EventDescriptionFragment();
            //Bundle bundle = new Bundle();
            //bundle.putString("eventName", events.get(position).getName());
            //bundle.putInt("eventImg", events.get(position).getImage());
            //bundle.putString("eventDesc", events.get(position).getDescription());
            //fragment.setArguments(bundle);

            //getFragmentManager().beginTransaction().replace(R.id.pager_container, fragment).addToBackStack(null).commit();
            //FragmentReplacer.switchFragment(getFragmentManager(), fragment, true);

            Intent intent = new Intent(getActivity(), EventDescriptionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("eventName", events.get(position).getName());
            bundle.putInt("eventImg", events.get(position).getImage());
            bundle.putString("eventDesc", events.get(position).getDescription());
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    };

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
