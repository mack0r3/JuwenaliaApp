package com.mpier.juvenaliaapp.AttractionsActivityTools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mpier.juvenaliaapp.R;

/**
 * Created by Korzonkie on 2016-02-15.
 */
public class CustomAdapter extends ArrayAdapter {

    Attraction[] attractions = null;

    public CustomAdapter(Context context, int resource, Attraction[] attractions) {
        super(context, R.layout.attraction_row, attractions);
        this.attractions = attractions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.attraction_row, parent, false);
        }

        TextView attractionName = (TextView)convertView.findViewById(R.id.attractionName);
        attractionName.setText(attractions[position].getAttrName());
        convertView.findViewById(R.id.attractionRowImage).setBackgroundResource(attractions[position].getAttrImgRes());
        Drawable background = convertView.findViewById(R.id.attractionRowImage).getBackground();
        background.setAlpha(50);

        return convertView;
    }
}
