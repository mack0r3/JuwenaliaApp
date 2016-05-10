package com.mpier.juvenaliaapp.LineUp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpier.juvenaliaapp.R;

import java.util.ArrayList;

/**
 * Created by Selve on 16.02.2016.
 */
public class LineUpAdapter extends BaseAdapter {

    private ArrayList<Event> events = new ArrayList<Event>();
    private Context context;

    public LineUpAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(context);
        View customView;

        if (convertView == null) {
            customView = myInflater.inflate(R.layout.line_up_custom_row, parent, false);
        } else {
            customView = convertView;
        }

        Event event = getItem(position);

        //RelativeLayout rlayout = (RelativeLayout) customView.findViewById(R.id.event_row_image);
        //rlayout.setBackgroundResource(event.getImage());
        //Drawable background = rlayout.getBackground();
        //background.setAlpha(50);

        ImageView iv = (ImageView) customView.findViewById(R.id.event_row_image);
        iv.setImageResource(event.getImage());

        ((TextView) customView.findViewById(R.id.event_name)).setText(event.getName());
        ((TextView) customView.findViewById(R.id.event_time)).setText(event.getTime());

        return customView;
    }
}

