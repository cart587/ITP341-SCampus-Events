package com.example.chris.itp341_finalproject_scampus_events.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chris.itp341_finalproject_scampus_events.R;

import java.util.ArrayList;

/**
 * Created by Chris on 12/7/2015.
 */
public class EventArrayAdapter extends ArrayAdapter<CampusEvent> {
    TextView eventTitleTextView;
    TextView eventLocationTextView;
    TextView eventTimeTextView;
    LinearLayout eventTextBackground;
    Context context;

    public EventArrayAdapter(Context context, ArrayList<CampusEvent> arrayListEvents) {
        super(context, 0, arrayListEvents);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CampusEvent campusEvent = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.event_custom_adapter_view,
                    parent,
                    false);
        }

        eventTitleTextView = (TextView) convertView.findViewById(R.id.event_title);
        eventLocationTextView = (TextView) convertView.findViewById(R.id.event_location);
        eventTimeTextView = (TextView) convertView.findViewById(R.id.event_time);
        eventTextBackground = (LinearLayout) convertView.findViewById(R.id.eventTextBackground);

        eventTitleTextView.setText(campusEvent.getEventTitle());
        eventLocationTextView.setText(campusEvent.getEventLocation());
        eventTimeTextView.setText(campusEvent.getEventTime());
        eventTextBackground.setBackgroundColor(ContextCompat.getColor(context, position % 2 == 0 ? R.color.transparent_cardinal : R.color.transparent_gold));

        return convertView;
    }
}
