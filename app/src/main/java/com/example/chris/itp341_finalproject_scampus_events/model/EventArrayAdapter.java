package com.example.chris.itp341_finalproject_scampus_events.model;

import android.content.Context;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chris.itp341_finalproject_scampus_events.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Chris on 12/7/2015.
 */
public class EventArrayAdapter extends  ArrayAdapter<CampusEvent>{
    TextView eventTitleTextView;
    TextView eventLocationTextView;
    TextView eventTimeTextView;
    ImageView eventImage;
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
        eventImage = (ImageView) convertView.findViewById(R.id.event_image);


        eventTitleTextView.setText(campusEvent.getEventTitle().toUpperCase());


        if (campusEvent.getBitmapImage() != null) {
            eventImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            eventImage.setImageBitmap(campusEvent.getBitmapImage());
        }

        eventLocationTextView.setText(campusEvent.getTextLocation().substring(0,campusEvent.getTextLocation().indexOf('\n')));
        if (campusEvent.getStartDate().getTime().after(Calendar.getInstance().getTime())) {
            //Event is not today
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
            eventTimeTextView.setText(sdf.format(campusEvent.getStartDate().getTime()));
        } else {
            String format;
            if (campusEvent.getStartDate().get(Calendar.HOUR_OF_DAY) >= 18)
                format = "Tonight at ";
            else
                format = "Today at ";

            eventTimeTextView.setText(format + DateFormat.getTimeInstance(DateFormat.SHORT).format(campusEvent.getStartDate().getTime()));
        }
        eventTextBackground.setBackgroundColor(ContextCompat.getColor(context, position % 2 == 0 ? R.color.transparent_cardinal : R.color.transparent_gold));

        return convertView;
    }
}
