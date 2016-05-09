package com.mpier.juvenaliaapp.LineUp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpier.juvenaliaapp.R;

/**
 * Created by Selve on 2016-05-09.
 */
public class EventDescriptionFragment extends Fragment {

    View inflatedView;

    public EventDescriptionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_lineup_description, container, false);
        }

        String eventName = "", eventDesc = "";
        Integer eventImg = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventName = bundle.getString("eventName", "");
            eventImg = bundle.getInt("eventImg");
            eventDesc = bundle.getString("eventDesc", "");
        }

        TextView eventNameTextView = (TextView) inflatedView.findViewById(R.id.event_name);
        TextView eventDescriptionTextView = (TextView) inflatedView.findViewById(R.id.event_description);

        eventNameTextView.setText(eventName);
        eventDescriptionTextView.setText(eventDesc);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            ImageView eventImageImageView = (ImageView) inflatedView.findViewById(R.id.event_img);
            eventImageImageView.setImageResource(eventImg);
        }

        return inflatedView;
    }
}
