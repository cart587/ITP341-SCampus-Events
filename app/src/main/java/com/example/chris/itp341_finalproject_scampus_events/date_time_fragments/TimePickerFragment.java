package com.example.chris.itp341_finalproject_scampus_events.date_time_fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.example.chris.itp341_finalproject_scampus_events.EventCreateActivity;
import com.example.chris.itp341_finalproject_scampus_events.event_create_fragments.EditDetailFragment;

import java.util.Calendar;

/**
 * Created by Chris on 12/12/2015.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Bundle bundle = getArguments();
        int viewId = bundle.getInt(EditDetailFragment.TIME, -1);

        Log.d("String - TimePickerFrag", hourOfDay + "");

        EventCreateActivity activity = (EventCreateActivity) getActivity();
        activity.updateTimeTextView(viewId, hourOfDay, minute);
    }
}
