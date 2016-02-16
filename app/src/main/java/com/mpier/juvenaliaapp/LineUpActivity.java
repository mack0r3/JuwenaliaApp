package com.mpier.juvenaliaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class LineUpActivity extends AppCompatActivity {

    final Event fridayEvents[] = new Event[] {
            new Event("16:15", "Bomba Kaloryczna", R.drawable.default_image),
            new Event("16:55", "The Cookies", R.drawable.default_image),
            new Event("17:40", "Mesajah", R.drawable.default_image),
            new Event("19:00", "Fisz Emade", R.drawable.default_image),
            new Event("20:50", "Sidney Polak", R.drawable.default_image),
            new Event("22:40", "Brodka", R.drawable.default_image)
    };

    final Event saturdayEvents[] = new Event[] {
            new Event("16:15", "Pod Sufitem Dżungla", R.drawable.default_image),
            new Event("16:55", "Jutro Wieczorem", R.drawable.default_image),
            new Event("17:40", "Małpa", R.drawable.default_image),
            new Event("19:00", "The Dumpligs", R.drawable.default_image),
            new Event("20:20", "Lady Pank", R.drawable.default_image),
            new Event("22:00", "T.Love", R.drawable.default_image)
    };

    final Event events[][] = new Event[][] { fridayEvents, saturdayEvents};

    final String days[] = new String[] {"PIĄTEK", "SOBOTA"};

    final int textViews[] = new int[] { R.id.events_text_view_friday, R.id.events_text_view_saturday };

    final int listViews[] = new int[] { R.id.events_list_view_friday, R.id.events_list_view_saturday };

    LineUpAdapter eventAdapter[] = new LineUpAdapter[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_up);

        for(int i = 0; i < 2; i++) {
            TextView dayNameTextView = (TextView) findViewById(textViews[i]);
            dayNameTextView.setText(days[i]);
            eventAdapter[i] = new LineUpAdapter(this, events[i]);
            ListView listView = (ListView) findViewById(listViews[i]);
            listView.setAdapter(eventAdapter[i]);
            ListUtils.setDynamicHeight(listView);
        }

    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(mListView.getWidth(), MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
