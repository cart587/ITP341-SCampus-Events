package com.example.chris.itp341_finalproject_scampus_events.event_create_fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chris.itp341_finalproject_scampus_events.EventCreateActivity;
import com.example.chris.itp341_finalproject_scampus_events.R;
import com.example.chris.itp341_finalproject_scampus_events.date_time_fragments.DatePickerFragment;
import com.example.chris.itp341_finalproject_scampus_events.date_time_fragments.TimePickerFragment;

import java.util.Calendar;

/**
 * Created by Chris on 12/12/2015.
 */
public class EditDetailFragment extends Fragment {

    EventCreateActivity eventCreateActivity;

    //UI VARIABLES FOR TITLE, DESCRIPTION, DATE, AND TIME
    EditText editTextEventTitle;
    TextView textViewEventStartDate;
    TextView textViewEventStartTime;
    TextView textViewEventEndDate;
    TextView textViewEventEndTime;
    EditText editTextEventDescription;

    final static public String DATE = "datePicker";
    final static public String TIME = "timePicker";
    final public String START_DATE = "startDatePicker";
    final public String START_TIME = "startTimePicker";
    final public String END_DATE = "endDatePicker";
    final public String END_TIME = "endTimePicker";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.event_edit_information_view,null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventCreateActivity = (EventCreateActivity) getActivity();
        eventCreateActivity.editDetailFragment = this;
        findAllViews();
        if(savedInstanceState == null)
            dateAndTimePickerSetUp();
    }

    public String getTitle() {
        return editTextEventTitle.getText().toString();
    }

    public String getDescription() {
        return editTextEventDescription.getText().toString();
    }

    private void dateAndTimePickerSetUp(){
        //INITIALIZE TO CURRENT DATE AND TIME
        Calendar currentDate = Calendar.getInstance();

        //Initial start date and time
        eventCreateActivity.updateDateTextView(R.id.text_view_start_date,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));

        eventCreateActivity.updateTimeTextView(R.id.text_view_start_time,
                currentDate.get(Calendar.HOUR_OF_DAY),
                currentDate.get(Calendar.MINUTE));

        Log.d("String",currentDate.get(Calendar.HOUR_OF_DAY)+"");
        //Initial end date and time
        Calendar endDateTime = (Calendar) currentDate.clone();
        endDateTime.add(Calendar.HOUR,1);

        eventCreateActivity.updateDateTextView(R.id.text_view_end_date,
                endDateTime.get(Calendar.YEAR),
                endDateTime.get(Calendar.MONTH),
                endDateTime.get(Calendar.DAY_OF_MONTH));

        eventCreateActivity.updateTimeTextView(R.id.text_view_end_time,
                endDateTime.get(Calendar.HOUR_OF_DAY),
                endDateTime.get(Calendar.MINUTE));

        //START DATE AND TIME LISTENERS
        textViewEventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(DATE,R.id.text_view_start_date);
                newFragment.setArguments(bundle);

                newFragment.show(eventCreateActivity.getSupportFragmentManager(), START_DATE);
            }
        });

        textViewEventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(TIME,R.id.text_view_start_time);
                newFragment.setArguments(bundle);

                newFragment.show(eventCreateActivity.getSupportFragmentManager(), START_TIME);
            }
        });

        //END DATE AND TIME LISTENERS
        textViewEventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(DATE,R.id.text_view_end_date);
                newFragment.setArguments(bundle);

                newFragment.show(eventCreateActivity.getSupportFragmentManager(), END_DATE);
            }
        });

        textViewEventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(TIME,R.id.text_view_end_time);
                newFragment.setArguments(bundle);

                newFragment.show(eventCreateActivity.getSupportFragmentManager(), END_TIME);
            }
        });
    }

    private void findAllViews(){
        editTextEventTitle = (EditText) eventCreateActivity.findViewById(R.id.event_edit_title);
        textViewEventStartDate = (TextView) eventCreateActivity.findViewById(R.id.text_view_start_date);
        textViewEventStartTime = (TextView) eventCreateActivity.findViewById(R.id.text_view_start_time);
        textViewEventEndDate = (TextView) eventCreateActivity.findViewById(R.id.text_view_end_date);
        textViewEventEndTime = (TextView) eventCreateActivity.findViewById(R.id.text_view_end_time);
        editTextEventDescription = (EditText) eventCreateActivity.findViewById(R.id.event_edit_description);
    }
}
