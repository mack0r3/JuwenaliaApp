package com.mpier.juvenaliaapp.Attractions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpier.juvenaliaapp.R;

/**
 * Created by Korzonkie on 2016-02-15.
 */
public class CustomAdapter extends ArrayAdapter {

    View convertView;

    Attraction[] attractions = null;

    TextView attractionName;
    ImageView attractionImage;

    public CustomAdapter(Context context, int resource, Attraction[] attractions) {
        super(context, R.layout.attraction_row, attractions);
        this.attractions = attractions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.attraction_row, parent, false);
        }

        this.convertView = convertView;

        initializeAndSetTextView(position);
        initializeAndSetImageView(position);

        return convertView;
    }

    private void initializeAndSetTextView(int index) {
        TextView attractionName = (TextView) convertView.findViewById(R.id.attractionName);
        attractionName.setText(attractions[index].getAttrName());
    }

    private void initializeAndSetImageView(int index) {
        ImageView attractionImage = (ImageView) convertView.findViewById(R.id.attractionRowImage);
        attractionImage.setImageResource(attractions[index].getAttrImgRes());
    }
}
