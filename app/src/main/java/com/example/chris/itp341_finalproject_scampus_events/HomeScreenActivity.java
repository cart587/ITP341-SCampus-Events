package com.example.chris.itp341_finalproject_scampus_events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chris.itp341_finalproject_scampus_events.model.CampusEventSingleton;
import com.example.chris.itp341_finalproject_scampus_events.model.EventArrayAdapter;

/**
 * Created by Chris on 12/8/2015.
 */
public class HomeScreenActivity extends Activity{
    //UI VARIABLES
    ListView eventListView;
    EventArrayAdapter arrayAdapter;

    //OTHER VARIABLES
    public static final String TAG = "HomeScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_view);

        findAllViews();
        setListenersAndAdapters();
    }

    private  void setListenersAndAdapters(){
        arrayAdapter = new EventArrayAdapter(this, CampusEventSingleton.getInstance(this).getCampusEvents());
        eventListView.setAdapter(arrayAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentEventDetail = new Intent(getApplicationContext(),EventDetailActivity.class);
                intentEventDetail.putExtra(EventDetailActivity.EVENT_EXTRA,position);
                startActivity(intentEventDetail);
            }
        });
    }

    private void findAllViews(){
        eventListView = (ListView) findViewById(R.id.listview_events);
    }
}
