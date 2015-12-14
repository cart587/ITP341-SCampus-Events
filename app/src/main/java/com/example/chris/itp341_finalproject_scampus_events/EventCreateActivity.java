package com.example.chris.itp341_finalproject_scampus_events;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.itp341_finalproject_scampus_events.event_create_fragments.EditDetailFragment;
import com.example.chris.itp341_finalproject_scampus_events.event_create_fragments.EditEventPhotoFragment;
import com.example.chris.itp341_finalproject_scampus_events.event_create_fragments.EditLocationFragment;
import com.example.chris.itp341_finalproject_scampus_events.event_create_fragments.EventCreatorPageAdapter;
import com.example.chris.itp341_finalproject_scampus_events.event_create_fragments.SlidingTabLayout;
import com.example.chris.itp341_finalproject_scampus_events.model.CampusEvent;
import com.example.chris.itp341_finalproject_scampus_events.model.CampusEventSingleton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Chris on 12/12/2015.
 */
public class EventCreateActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EventCreatorPageAdapter pagerAdapter;
    private ViewPager mViewPager;
    private SlidingTabLayout tabs;
    private CharSequence[] titles;

    public Calendar startDate = null;
    public Calendar endDate = null;
    public Location eventLocation;
    public String textLocation;

    public EditDetailFragment editDetailFragment;
    public EditLocationFragment editLocationFragment;
    public EditEventPhotoFragment editEventPhotoFragment;

    CampusEventSingleton campusEventSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create_view);

        titles = getResources().getStringArray(R.array.tab_names);
        editDetailFragment = null;
        editLocationFragment = null;
        editEventPhotoFragment = null;
        pagerAdapter = new EventCreatorPageAdapter(getSupportFragmentManager(),titles);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pagerAdapter);

        actionBarSetUp();
    }

    private void saveEvent() {
        CampusEvent event = new CampusEvent();
        saveEventDetails(event);
        saveEventLocation(event);
        saveEventPhoto(event);

        Log.d("EventString", event.getTextLocation());
        CampusEventSingleton.getInstance(this).addCampusEvent(event);

        setResult(RESULT_OK);
        finish();
    }

    private void saveEventDetails(CampusEvent event) {
        if (editDetailFragment != null) {
            event.setEventTitle(editDetailFragment.getTitle());
            event.setEventDescription(editDetailFragment.getDescription());
            event.setStartDate(startDate);
            event.setEndDate(endDate);
        }
    }

    public void saveEventLocation(CampusEvent event) {
        event.setLocation(eventLocation);
        event.setTextLocation(textLocation);
        Log.d("EventCreateActivity", textLocation);
    }

    private void saveEventPhoto(CampusEvent event) {
        event.setBitmapImage(editEventPhotoFragment.getEventBitMapImage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.done_creating_event:

                saveEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void updateDateTextView(int viewId, int year, int month, int day){
        if(viewId != -1) {
            TextView textViewDisplayDate = (TextView) mViewPager.findViewById(viewId);

            Calendar tempDate = new GregorianCalendar(year, month, day);
            DateFormat dateFormat = DateFormat.getDateInstance();
            String date = dateFormat.format(tempDate.getTime());

            String dayOfWeek = tempDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

            textViewDisplayDate.setText(dayOfWeek + ", " + date);

            switch (viewId){
                case R.id.text_view_start_date:
                    startDate = (Calendar) tempDate.clone();
                    break;
                case R.id.text_view_end_date:
                    endDate = (Calendar) tempDate.clone();
            }
        }else{
            Toast.makeText(this,"Cannot find date view",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTimeTextView(int viewId, int hour, int minute){
        if(viewId != -1) {
            TextView textViewDisplayTime = (TextView) mViewPager.findViewById(viewId);

            DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
            String formattedTime = "";

            switch (viewId){
                case R.id.text_view_start_time:
                    startDate.set(Calendar.HOUR_OF_DAY,hour);
                    startDate.set(Calendar.MINUTE,minute);
                    formattedTime = dateFormat.format(startDate.getTime());
                    break;
                case R.id.text_view_end_time:
                    endDate.set(Calendar.HOUR_OF_DAY, hour);
                    endDate.set(Calendar.MINUTE,minute);
                    formattedTime = dateFormat.format(endDate.getTime());
            }

            textViewDisplayTime.setText(formattedTime);
        }else{
            Toast.makeText(this,"Cannot find time view",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void actionBarSetUp(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(mViewPager);
    }
}
