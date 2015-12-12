package com.example.chris.itp341_finalproject_scampus_events;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.chris.itp341_finalproject_scampus_events.model.CampusEventSingleton;
import com.example.chris.itp341_finalproject_scampus_events.model.EventArrayAdapter;
import com.example.chris.itp341_finalproject_scampus_events.slider_model.SliderArrayAdapter;
import com.example.chris.itp341_finalproject_scampus_events.slider_model.SliderCategory;

import java.util.ArrayList;

/**
 * Created by Chris on 12/8/2015.
 */
public class HomeScreenActivity extends AppCompatActivity {
    //UI VARIABLES
    ListView eventListView;
    EventArrayAdapter arrayAdapter;

    //SLIDING DRAWER VARIABLES
    private ListView mDrawerList;
    private SliderArrayAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    //OTHER VARIABLES
    public static final String TAG = "HomeScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        findAllViews();
        setListenersAndAdapters();
        addDrawerItems();
        setupDrawer();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawerItems() {

        SliderCategory category = new SliderCategory("Create Event",getDrawable(R.drawable.ic_add_black_18dp));
        ArrayList<SliderCategory> arrCategories = new ArrayList<>();

        arrCategories.add(category);

        mAdapter = new SliderArrayAdapter(this, arrCategories);
        mDrawerList.setAdapter(mAdapter);
    }

    private  void setListenersAndAdapters(){
        //Main UI
        arrayAdapter = new EventArrayAdapter(this, CampusEventSingleton.getInstance(this).getCampusEvents());
        eventListView.setAdapter(arrayAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentEventDetail = new Intent(getApplicationContext(), EventDetailActivity.class);
                intentEventDetail.putExtra(EventDetailActivity.EVENT_EXTRA, position);
                startActivity(intentEventDetail);
            }
        });



    }

    private void findAllViews(){
        eventListView = (ListView) findViewById(R.id.listview_events);

        //Sliding Drawer UI
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
