package com.mpier.juvenaliaapp.LineUpActivityTools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpier.juvenaliaapp.R;

/**
 * Created by Selve on 16.02.2016.
 */
public class LineUpAdapter extends BaseAdapter {

    Event[] events;
    Context context;

    public LineUpAdapter(Context context, Event[] events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.length;
    }

    @Override
    public Event getItem(int position) {
        return events[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(context);
        View customView;

        if(convertView == null) {
            customView = myInflater.inflate(R.layout.line_up_custom_row, parent, false);
        } else {
            customView = convertView;
        }

        Event event = getItem(position);

        RelativeLayout rlayout = (RelativeLayout) customView.findViewById(R.id.event_row_image);
        rlayout.setBackgroundResource(event.getImage());
        Drawable background = rlayout.getBackground();
        background.setAlpha(50);

        ((TextView) customView.findViewById(R.id.event_name)).setText(event.getName());
        ((TextView) customView.findViewById(R.id.event_time)).setText(event.getTime());

        return customView;
    }
}

